
// Lagrer index-verdier i datastrukturen.

package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.scanner.Scanner;
import static no.uio.ifi.asp.scanner.TokenKind.*;
import no.uio.ifi.asp.runtime.*;

import java.util.ArrayList;

public class AspSubscription extends AspPrimarySuffix
{
  AspExpr body;

  AspSubscription(int n)
  {
    super(n);
  }

  static AspSubscription parse(Scanner s)
  {
    enterParser("Subscription expression");
    AspSubscription aps = new AspSubscription(s.curLineNum());

    skip(s, leftBracketToken);
    aps.body = AspExpr.parse(s);
    skip(s, rightBracketToken);

    leaveParser("Subscription expression");
    return aps;
  }

  @Override
  void prettyPrint()
  {
    prettyWrite("[");
    body.prettyPrint();
    prettyWrite("]");
  }

  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue
  {
    return body.eval(curScope);
  }
}
