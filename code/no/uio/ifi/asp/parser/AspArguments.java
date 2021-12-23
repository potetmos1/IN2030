
// Denne klassen lagrer arguments i datastrukturen.
// Verdiene i AspArguments evalueres i funksjonskall.

package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.scanner.Scanner;
import static no.uio.ifi.asp.scanner.TokenKind.*;
import no.uio.ifi.asp.runtime.*;

import java.util.ArrayList;

public class AspArguments extends AspPrimarySuffix
{
  ArrayList <AspExpr> body;

  AspArguments(int n)
  {
    super(n);
  }

  static AspArguments parse(Scanner s)
  {
    enterParser("Arguments expression");
    AspArguments aar = new AspArguments(s.curLineNum());
    aar.body = new ArrayList <> ();

    skip(s, leftParToken);
    if (s.curToken().kind != rightParToken)
    {
      aar.body.add(AspExpr.parse(s));
    }
    while (s.curToken().kind != rightParToken)
    {
      skip(s, commaToken);
      aar.body.add(AspExpr.parse(s));
    }
    skip(s, rightParToken);

    leaveParser("Arguments expression");
    return aar;
  }

  @Override
  void prettyPrint()
  {
    prettyWrite("(");
    int counter = 0;
    for (AspExpr x: body)
    {
      if (counter > 0)
      {
        prettyWrite(", ");
      }
      x.prettyPrint();
      counter++;
    }
    prettyWrite(")");
  }

  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue{
    ArrayList<RuntimeValue> returnList = new ArrayList<>();
    for (AspExpr x: body)
    {
      returnList.add(x.eval(curScope));
    }

    return new RuntimeListValue(returnList);
  }
}
