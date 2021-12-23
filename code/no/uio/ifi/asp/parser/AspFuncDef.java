
// Denne klassen oppretter datastruktur for funksjoner og lar
// dem kjøre i lokalt skop i samarbeid med RuntimeFunc.

package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.scanner.Scanner;
import static no.uio.ifi.asp.scanner.TokenKind.*;
import no.uio.ifi.asp.runtime.*;

import java.util.ArrayList;

public class AspFuncDef extends AspCompoundStmt
{
  AspName name;
  ArrayList <AspName> arguments;
  AspSuite body;

  AspFuncDef(int n)
  {
    super(n);
  }

  static AspFuncDef parse(Scanner s)
  {
    enterParser("Function definition");
    AspFuncDef afd = new AspFuncDef(s.curLineNum());
    afd.arguments = new ArrayList <> ();

    // Lagrer funksjonsnavn
    skip(s, defToken);
    afd.name = AspName.parse(s);
    skip(s, leftParToken);

    // Lagrer navn på formelle paramatre
    if (s.curToken().kind != rightParToken)
    {
      afd.arguments.add(AspName.parse(s));
    }
    while (s.curToken().kind != rightParToken)
    {
      skip(s, commaToken);
      afd.arguments.add(AspName.parse(s));
    }
    skip(s, rightParToken);
    skip(s, colonToken);

    // Lagrer innsiden av funksjonen
    afd.body = AspSuite.parse(s);
    leaveParser("Function definition");

    return afd;
  }

  @Override
  void prettyPrint()
  {
    prettyWrite("def ");
    name.prettyPrint();
    prettyWrite("(");
    int i = 0;
    for (AspName x: arguments)
    {
      if (i > 0)
      {
        prettyWrite(", ");
      }
      x.prettyPrint();
      i++;
    }
    prettyWrite("):");
    body.prettyPrint();
    prettyWriteLn();
  }

  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue{

    ArrayList<RuntimeValue> args = new ArrayList<>();

    RuntimeStringValue funcName = new RuntimeStringValue(name.nameVal);

    for (AspName formal: arguments)
    {
      args.add(new RuntimeStringValue(formal.nameVal));
    }

    // Lagrer formelle parametre i form av stringer i et nytt
    // funksjonsobjekt
    RuntimeFunc newFunction = new RuntimeFunc(funcName, args, curScope, this);
    curScope.assign(name.nameVal, newFunction);

    trace("def: " + newFunction.showInfo());

    return null;
  }

  // Prosedyrekroppen kjøres med et nytt skop som inneholder aktuelle parametre.
  // RuntimeFunc oppretter skopet og kaller denne funksjonen.
  public RuntimeValue runFunc(RuntimeScope curScope) throws RuntimeReturnValue{
    return body.eval(curScope);
  }
}
