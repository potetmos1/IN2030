
// Denne klassen setter globale variabler inn i datastrukturen
// og lagrer de i det globale skopet.

package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.scanner.Scanner;
import static no.uio.ifi.asp.scanner.TokenKind.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.TokenKind.*;

import java.util.ArrayList;

public class AspGlobalStmt extends AspSmallStmt
{
  ArrayList <AspName> value;

  AspGlobalStmt(int n)
  {
    super(n);
  }

  static AspGlobalStmt parse(Scanner s)
  {
    enterParser("Global statement");
    AspGlobalStmt ags = new AspGlobalStmt(s.curLineNum());
    ags.value = new ArrayList <> ();
    skip(s, globalToken);
    ags.value.add(AspName.parse(s));
    while (s.curToken().kind == commaToken)
    {
      skip(s, commaToken);
      ags.value.add(AspName.parse(s));
    }

    leaveParser("Global statement");
    return ags;
  }

  @Override
  void prettyPrint()
  {
    prettyWrite("global ");
    int i = 0;
    for (AspName x: value)
    {
      if (i > 0)
      {
        prettyWrite(", ");
      }
      x.prettyPrint();
    }
  }

  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue
  {
    // Setter variabelnavnene inn i listen med globale variabler
    for (AspName x: value)
    {
      curScope.registerGlobalName(x.nameVal);
    }
    return null;
  }
}
