
// Denne klassen kaller kun på AspExpr

package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.scanner.Scanner;
import static no.uio.ifi.asp.scanner.TokenKind.*;
import no.uio.ifi.asp.runtime.*;

import java.util.ArrayList;

public class AspExprStmt extends AspSmallStmt
{
  AspExpr value;

  AspExprStmt(int n)
  {
    super(n);
  }

  static AspExprStmt parse(Scanner s)
  {
    enterParser("Expr statement");
    AspExprStmt ars = new AspExprStmt(s.curLineNum());

    ars.value = AspExpr.parse(s);

    leaveParser("Expr statement");
    return ars;
  }

  @Override
  void prettyPrint()
  {
    value.prettyPrint();
  }

  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue
  {
    return value.eval(curScope);
  }
}
