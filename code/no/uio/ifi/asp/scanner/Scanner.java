// © 2021 Dag Langmyhr, Institutt for informatikk, Universitetet i Oslo


package no.uio.ifi.asp.scanner;

import java.io.*;
import java.util.*;

import javax.swing.plaf.InsetsUIResource;

import no.uio.ifi.asp.main.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class Scanner
{
    private LineNumberReader sourceFile = null;
    private String curFileName;
    private ArrayList < Token > curLineTokens = new ArrayList < > ();
    private Stack < Integer > indents = new Stack < > ();
    private final int TABDIST = 4;


    public Scanner(String fileName)
    {
        curFileName = fileName;
        indents.push(0);

        try
        {
            sourceFile = new LineNumberReader(
                new InputStreamReader(
                    new FileInputStream(fileName),
                    "UTF-8"));
        }
        catch (IOException e)
        {
            scannerError("Cannot read " + fileName + "!");
        }
    }


    private void scannerError(String message)
    {
        String m = "Asp scanner error";
        if (curLineNum() > 0)
            m += " on line " + curLineNum();
        m += ": " + message;

        Main.error(m);
    }


    public Token curToken()
    {
        while (curLineTokens.isEmpty())
        {
            readNextLine();
        }
        return curLineTokens.get(0);
    }

    //kalles paa fra Main.java
    public void readNextToken()
    {
        if (!curLineTokens.isEmpty())
            curLineTokens.remove(0);
    }

    //finne string i linje
    public void stringFind(String line, ArrayList<Token> tokens, ArrayList<Boolean> tokenBooleans)
    {
      boolean isQString = false; // '' string
      boolean isQQString = false; // "" string
      char lineSplit[] = line.toCharArray();
      String currentString = "";

      for(int i=0; i<line.length(); i++)
      {
        if ((isQQString && (lineSplit[i] != '\"')) || (isQString && (lineSplit[i] != '\'')))
        {
          currentString += lineSplit[i];
          tokenBooleans.set(i, true);
        }

        // Starter/avslutter string
        if (!isQQString && (lineSplit[i] == '\''))
        {
          if (isQString) //skiller mellom "" og ''
          {
            Token insertString = new Token(stringToken);
            insertString.stringLit = currentString;
            insertString.lineNum = curLineNum();
            tokens.set(i, insertString);
            currentString = "";
          }
          isQString = !isQString;
          tokenBooleans.set(i, true);
        }
        if (!isQString && (lineSplit[i] == '\"'))
        {
          if (isQQString) //skiller mellom "" og ''
          {
            Token insertString = new Token(stringToken);
            insertString.stringLit = currentString;
            insertString.lineNum = curLineNum();
            tokens.set(i, insertString);
            currentString = "";
          }
          isQQString = !isQQString;
          tokenBooleans.set(i, true);
        }
      }

      // Dersom en string er aktiv på slutten er det en syntax feil
      if (isQString || isQQString)
      {
        scannerError("String not closed");
      }
    }

    //Finner name variables i linjen
    public void nameFind(String line, ArrayList<Token> tokens, ArrayList<Boolean> tokenBooleans){
        char lineSplit[] = line.toCharArray();
        String currentName = "";
        Token newToken = null;
        Boolean isName = false;

        for(int i=0; i<line.length(); i++){
            if((isLetterAZ(lineSplit[i]) || lineSplit[i] == '_' ) && !tokenBooleans.get(i)){ //la til sjekk for om den er brukt i string tidligere
                currentName += lineSplit[i];
                tokenBooleans.set(i, true);
                isName = true;
            }
            else if(isDigit(lineSplit[i]) && isName)
            {
              currentName += lineSplit[i];
              tokenBooleans.set(i, true);
            }
            else if(currentName.length() > 0){
                boolean foundMatch = false;

                for(TokenKind kind:TokenKind.values())
                {
                  if (currentName.equals(kind.image))
                  {
                    newToken = new Token(kind);
                    foundMatch = true;
                  }
                }

                if(!foundMatch){
                    newToken = new Token(nameToken); //kan være "name"
                    newToken.name = currentName;
                }
                newToken.lineNum = curLineNum();
                tokens.set(i-1, newToken);
                isName = false;
                currentName = "";
            }
        }

        if(currentName.length() > 0){
            boolean foundMatch = false;

            for(TokenKind kind:TokenKind.values())
            {
              if (currentName.equals(kind.image))
              {
                newToken = new Token(kind);
                foundMatch = true;
              }
            }

            if(!foundMatch){
                newToken = new Token(nameToken); //kan være "name"
                newToken.name = currentName;
            }
            newToken.lineNum = curLineNum();
            tokens.set(line.length()-1, newToken);
        }

    }

    //finnes tall i linjen
    public void numberFind(String line, ArrayList<Token> tokens, ArrayList<Boolean> tokenBooleans){
        String currentNumber ="";
        Token newToken = null;
        char lineSplit[] = line.toCharArray();

        for(int i=0; i<lineSplit.length; i++){
            if((isDigit(lineSplit[i]) || lineSplit[i] == '.') && !tokenBooleans.get(i)){
                currentNumber+=lineSplit[i];
                tokenBooleans.set(i, true);
            }else if(currentNumber.length() > 0){
                if(currentNumber.contains(".")){
                    float value = Float.parseFloat(currentNumber);
                    newToken = new Token(floatToken, curLineNum());
                    newToken.floatLit = value;
                }else{
                    int value = Integer.parseInt(currentNumber);
                    newToken = new Token(integerToken, curLineNum());
                    newToken.integerLit = value;
                }
                tokens.set(i-1, newToken);
                currentNumber = "";
            }
        }
        if(currentNumber.length() > 0){
            if(currentNumber.contains(".")){
                float value = Float.parseFloat(currentNumber);
                newToken = new Token(floatToken, curLineNum());
                newToken.floatLit = value;
            }else{
                int value = Integer.parseInt(currentNumber);
                newToken = new Token(integerToken, curLineNum());
                newToken.integerLit = value;
            }
            tokens.set(line.length()-1, newToken);
        }
    }

    //finner riktig indentering. Må kalles etter expandLeadingTabs().
    public void indentFind(String line, ArrayList<Boolean> tokenBooleans)
    {
        int spaces = findIndent(line);
        int previous = indents.peek();

        // markerer alle mellomrom
        for(int i=0; i<line.length(); i++)
        {
          if (line.charAt(i) == ' ' || line.charAt(i) == '\t')
          {
            tokenBooleans.set(i, true);
          }
        }

        // logikk for indentering
        if (spaces > previous)
        {
          indents.push(spaces);
          curLineTokens.add(0, new Token(indentToken, curLineNum()));
        }
        else if (spaces < previous)
        {
          int dedents = 0;
          while (spaces < indents.peek())
          {
            indents.pop();
            curLineTokens.add(dedents, new Token(dedentToken, curLineNum()));
            dedents++;
            // ikke plass i begynnelsen av tokens array så legges til direkte
          }
        }

        if (spaces != indents.peek())
        {
          //indentation error
          scannerError("Unbalanced indentation");
        }
    }

    //finner operatorer
    public void bracketFind(String line, ArrayList<Token> tokens, ArrayList<Boolean> tokenBooleans)
    {
        char brackets[] = new char[]{'(','[','{','}',']',')'};
        TokenKind type[] = new TokenKind[]
        {
          leftParToken,
          leftBracketToken,
          leftBraceToken,
          rightBraceToken,
          rightBracketToken,
          rightParToken
        };
        for(int i=0; i<line.length(); i++)
        {
          if (tokenBooleans.get(i))
          {
            continue;
          }
          for (int j=0; j<brackets.length; j++)
          {
            if (line.charAt(i) == brackets[j])
            {
              tokens.set(i, new Token(type[j], curLineNum()));
              tokenBooleans.set(i, true);
            }
          }
        }
    }

    //finner operatorer
    // kalles sist. Nå skal det bare være operatorer igjen.
    public void operatorFind(String line, ArrayList<Token> tokens, ArrayList<Boolean> tokenBooleans)
    {
        char lineSplit[] = line.toCharArray();
        String currentOp = "";
        Token newToken = null;

        for(int i=0; i<line.length(); i++)
        {
          if (!tokenBooleans.get(i))
          {
            currentOp += lineSplit[i];
            tokenBooleans.set(i, true);
          }
          else if(currentOp.length() > 0)
          {
            boolean foundMatch = false;
            for(TokenKind kind:TokenKind.values())
            {
              if(currentOp.equals(kind.image))
              {
                newToken = new Token(kind);
                foundMatch = true;
              }
            }
            //hvis token ikke finnes, har den lagt sammen for mange symboler sannsynligvis
            if(!foundMatch)
            {
              String op1;
              String op2;

              if(currentOp.length() == 2){
                op1 = currentOp.substring(0, 1);
                op2 = currentOp.substring(1);


              }else{
                op1 = currentOp.substring(0,2);
                op2 = currentOp.substring(2);
              }
              for(TokenKind kind:TokenKind.values()){
                if(op1.equals(kind.image)){
                  Token newTokenSplit1 = new Token(kind);
                  foundMatch = true;
                  newTokenSplit1.lineNum = curLineNum();
                  tokens.set(i-2, newTokenSplit1);

                }
              }

              for(TokenKind kind:TokenKind.values()){
                if(op2.equals(kind.image)){
                  Token newTokenSplit2 = new Token(kind);
                  foundMatch = true;
                  newTokenSplit2.lineNum = curLineNum();
                  tokens.set(i-1, newTokenSplit2);
                }
              }
            }
            currentOp = "";
          }
          if (newToken != null)
          {
            newToken.lineNum = curLineNum();
            tokens.set(i-1, newToken);
            newToken = null;
          }
        }

        if (currentOp != "")
        {
          for(TokenKind kind:TokenKind.values())
          {
            if(currentOp.equals(kind.image))
            {
              Token lastToken = new Token(kind);
              lastToken.lineNum = curLineNum();
              tokens.set(line.length()-1, lastToken);
            }
          }
        }
    }

    private void readNextLine()
    {
        curLineTokens.clear();

        // Read the next line:
        String line = null;
        try
        {
            line = sourceFile.readLine();
            if (line == null)
            {
                sourceFile.close();
                sourceFile = null;

                //Legger til dedent tokens filen når slutten
                while (indents.peek() > 0)
                {
                  Token newToken = new Token(dedentToken, curLineNum());
                  curLineTokens.add(newToken);
                  indents.pop();
                  Main.log.noteToken(newToken);
                }

                Token EOFToken = new Token(eofToken, curLineNum());
                curLineTokens.add(EOFToken);
                Main.log.noteToken(EOFToken);
                return;
            }
            else
            {
                Main.log.noteSourceLine(curLineNum(), line);
            }
        }
        catch (IOException e)
        {
            sourceFile = null;
            scannerError("Unspecified I/O error!");
        }

        // Endringer del 1

        // Fjerner kommentarer
        line = line.split("#")[0];

        // Gjør tabs til spaces
        line = expandLeadingTabs(line);


        // Hopper over tomme linjer
        int lineLength = line.length();
        if (findIndent(line) >= lineLength)
        {
          return;
        }

        // Oppretter en ekstra liste for å markere hvilke tegn det er
        // laget tokens av
        ArrayList <Token> insertTokens = new ArrayList <> (lineLength);
        ArrayList <Boolean> insertBoolean = new ArrayList <> (lineLength);

        for(int i=0; i<lineLength; i++){
            insertTokens.add(null);
            insertBoolean.add(false);
        }

        indentFind(line, insertBoolean);


        // Markerer spaces
        char lineSplit[] = line.toCharArray();
        for(int i=0; i<lineLength; i++)
        {
          if (lineSplit[i] == ' ')
          {
            insertBoolean.set(i, true);
          }
        }

        // Her opprettes de fleste token-objektene.
        // Linjen itereres gjennom flere ganger for at hvert symbol
        // skal ha riktig prioritet når de gjøres om til tokens.
        // Deler av linjen som er gjort om til tokens markeres i
        // tilsvarende posisjon i insertBoolean slik at f.eks
        // tall innenfor strings ikke oppretter nye tokens.
        stringFind(line, insertTokens, insertBoolean);
        nameFind(line, insertTokens, insertBoolean);
        numberFind(line, insertTokens, insertBoolean);
        bracketFind(line, insertTokens, insertBoolean);
        operatorFind(line, insertTokens, insertBoolean);

        for(Token t: insertTokens)
        {
          if(t != null)
          {
            curLineTokens.add(t);
          }
        }

        curLineTokens.add(new Token(newLineToken, curLineNum()));

        for (Token t: curLineTokens)
        {
          Main.log.noteToken(t);
        }
    }

    public int curLineNum()
    {
        return sourceFile != null ? sourceFile.getLineNumber() : 0;
    }

    private int findIndent(String s)
    {
        int indent = 0;

        while (indent < s.length() && s.charAt(indent) == ' ') indent++;
        return indent;
    }

    private String expandLeadingTabs(String s)
    {
        int spaceCounter = 0;

        // Loopen iterer gjennom mellomrom og tabs, returnerer når den
        // finner et annet tegn
        for (int i = 0; i < s.length(); i++)
        {
          char currentChar = s.charAt(i);
          if (currentChar == ' ')
          {
            spaceCounter++;
          }
          else if (currentChar == '\t')
          {
            // Følger algoritmen i kompendiet for IN2030.
            // 1-3 spaces før tab blir ignorert.
            int replacementNumber = TABDIST - (spaceCounter % TABDIST);
            spaceCounter += replacementNumber;
            // Oppretter char array med antall mellomrom som tabs skal byttes
            // ut med
            String replacement = new String(new char[replacementNumber]).replace("\0", " ");

            // Setter inn mellomrom for tabs (typisk 4)
            s = s.substring(0, i) + replacement + s.substring(i+1);

            // Oppdaterer string posisjonen med nye antall mellomrom
            i += replacementNumber - 1;
          }
          else
          {
            return s;
          }
        }

        return s;
    }


    private boolean isLetterAZ(char c)
    {
        return ('A' <= c && c <= 'Z') || ('a' <= c && c <= 'z') || (c == '_');
    }


    private boolean isDigit(char c)
    {
        return '0' <= c && c <= '9';
    }


    public boolean isCompOpr()
    {
        TokenKind k = curToken().kind;
        switch (k){
          case lessToken:
          case greaterToken:
          case doubleEqualToken:
          case lessEqualToken:
          case greaterEqualToken:
          case notEqualToken:
            return true;
          default:
            return false;
        }
    }


    public boolean isFactorPrefix()
    {
        TokenKind k = curToken().kind;
        return (k == TokenKind.plusToken || k == TokenKind.minusToken);
    }


    public boolean isFactorOpr()
    {
        TokenKind k = curToken().kind;
        String factOps[] = new String[]{"*", "/", "//", "%"};
        for (String x:factOps){
          if(k.image == x){
            return true;
          }
        }
        return false;
    }


    public boolean isTermOpr()
    {
        TokenKind k = curToken().kind;
        String compound[] = new String[]{"for","def","if","while"};
        for (String x: compound)
        {
          if (k.image == x)
          {
            return true;
          }
        }
        return false;
    }


    public boolean anyEqualToken()
    {
        for (Token t: curLineTokens)
        {
            if (t.kind == equalToken) return true;
            if (t.kind == semicolonToken) return false;
        }
        return false;
    }
}
