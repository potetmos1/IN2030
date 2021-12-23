
// Denne klassen oppretter datastruktur for while-loops og kjører dem.
// Denne filen bruker den foreslåtte implementasjonen fra lysbildene i
// in 2030.

package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.scanner.Scanner;
import static no.uio.ifi.asp.scanner.TokenKind.*;
import no.uio.ifi.asp.runtime.*;

import java.util.ArrayList;

public class AspWhileStmt extends AspCompoundStmt
{
  AspExpr test;
  AspSuite body;

  AspWhileStmt(int n)
  {
    super(n);
  }

  static AspWhileStmt parse(Scanner s)
  {
    enterParser("While statement");
    AspWhileStmt aws = new AspWhileStmt(s.curLineNum());
    // skip() gir feilmelding om det er feil token, ellers går den videre
    skip(s, whileToken);
    aws.test = AspExpr.parse(s);
    skip(s, colonToken);
    aws.body = AspSuite.parse(s);
    leaveParser("While statement");

    return aws;
  }

  @Override
  void prettyPrint()
  {
    prettyWrite("while ");
    test.prettyPrint();
    prettyWrite(":");
    body.prettyPrint();
  }

  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue
  {
    // Bryter ut av loopen dersom testverdien blir false
    while(true){
      RuntimeValue testValue = test.eval(curScope);
      if( !testValue.getBoolValue("while loop test", this)){
        break;
      }
      trace("while True:...");
      body.eval(curScope);
    }
    trace("while False:");
    return null;
  }
}
