
// Denne abstrakte klassen skiller mellom de ulike typene
// compound statements (for, while, if og def)

package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind.*;
import no.uio.ifi.asp.runtime.*;

import java.util.ArrayList;

abstract class AspCompoundStmt extends AspStmt
{
  AspCompoundStmt(int n)
  {
    super(n);
  }

  static AspCompoundStmt parse(Scanner s)
  {
    enterParser("Compound Statement");

    AspCompoundStmt acst = null;

    switch(s.curToken().kind)
    {
      case defToken:
        acst = AspFuncDef.parse(s);
        break;
      case ifToken:
        acst = AspIfStmt.parse(s);
        break;
      case forToken:
        acst = AspForStmt.parse(s);
        break;
      case whileToken:
        acst = AspWhileStmt.parse(s);
        break;
    }

    leaveParser("Compound Statement");

    return acst;
  }

  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue
  {
    return null;
  }
}
