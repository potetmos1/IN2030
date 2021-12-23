
// Denne abstrakte klassen skiller mellom AspCompoundStmt
// (for, if, while og def) og AspSmallStmtList
// som har ett eller flere enklere uttrykk.

package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.scanner.Scanner;
import static no.uio.ifi.asp.scanner.TokenKind.*;
import no.uio.ifi.asp.runtime.*;

import java.util.ArrayList;

abstract class AspStmt extends AspSyntax
{
  AspStmt(int n)
  {
    super(n);
  }

  static AspStmt parse(Scanner s)
  {
    enterParser("Statement");

    AspStmt ast = null;

    if (s.isTermOpr())
    {
      ast = AspCompoundStmt.parse(s);
    }
    else
    {
      ast = AspSmallStmtList.parse(s);
    }
    leaveParser("Statement");

    return ast;
  }
}
