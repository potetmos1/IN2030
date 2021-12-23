
// Denne abstrakte klassen skiller mellom
// tilordinger, global, return, pass og expressions.

package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;
import no.uio.ifi.asp.runtime.*;

import java.util.ArrayList;

abstract class AspSmallStmt extends AspSyntax
{

  AspSmallStmt(int n)
  {
    super(n);
  }

  static AspSmallStmt parse(Scanner s)
  {
    enterParser("Small statement");
    // skip() gir feilmelding om det er feil token, ellers går den videre

    AspSmallStmt ast = null;

    if (s.anyEqualToken())
    {
      ast = AspAssignment.parse(s);
    }
    else
    {
      switch(s.curToken().kind)
      {
        case globalToken:
          ast = AspGlobalStmt.parse(s);
          break;
        case returnToken:
          ast = AspReturnStmt.parse(s);
          break;
        case passToken:
          ast = AspPassStmt.parse(s);
          break;
        default:
          ast = AspExprStmt.parse(s);
          break;
      }
    }

    leaveParser("Small statement");

    return ast;
  }
}
