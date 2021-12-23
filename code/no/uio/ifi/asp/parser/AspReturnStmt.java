
// Denne klassen setter return statements inn i datastrukturen.
// Når de kjøres brytes det rekursive kallet som brukes i resten av
// implementasjonen ved å kalle på throw.

package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.scanner.Scanner;
import static no.uio.ifi.asp.scanner.TokenKind.*;
import no.uio.ifi.asp.runtime.*;

import java.util.ArrayList;

public class AspReturnStmt extends AspSmallStmt
{
  AspExpr value;
  int line;

  AspReturnStmt(int n)
  {
    super(n);
  }

  static AspReturnStmt parse(Scanner s)
  {
    enterParser("return statement");
    AspReturnStmt ars = new AspReturnStmt(s.curLineNum());

    skip(s, returnToken);
    ars.value = AspExpr.parse(s);

    leaveParser("return statement");
    return ars;
  }

  @Override
  void prettyPrint()
  {
    prettyWrite("return ");
    value.prettyPrint();
  }

  // throw tvinger return-verdien oppover i datastrukturen istedenfor
  // at det rekursive kallet fortsetter i den gjeldende grenen.
  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue{
    RuntimeValue v = value.eval(curScope);
    trace("return " + v.showInfo());
    throw new RuntimeReturnValue(v, line);
  }
}
