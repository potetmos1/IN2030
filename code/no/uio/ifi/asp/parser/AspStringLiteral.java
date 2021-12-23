package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;
import no.uio.ifi.asp.scanner.Scanner;

//String class for String values
public class AspStringLiteral extends AspAtom{
    String chars;

    AspStringLiteral(int num){
        super(num);
    }

    static AspStringLiteral parse(Scanner s){
        enterParser("String");
        AspStringLiteral strLit = new AspStringLiteral(s.curLineNum());
        strLit.chars = s.curToken().stringLit;
        s.readNextToken();
        leaveParser("String");
        return strLit;
    }

    @Override
    //chose that all String use "" instead of ''
    public void prettyPrint(){
        prettyWrite("\"");
        prettyWrite(chars);
        prettyWrite("\"");
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue{
        return new RuntimeStringValue(chars);
    }
}
