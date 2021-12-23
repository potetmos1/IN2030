
// Denne klassen oppretter datastruktur og kjører
// for-loops

package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.scanner.Scanner;
import static no.uio.ifi.asp.scanner.TokenKind.*;
import no.uio.ifi.asp.runtime.*;

import java.util.ArrayList;

public class AspForStmt extends AspCompoundStmt
{
  AspName var;
  AspExpr test;
  AspSuite body;

  AspForStmt(int n)
  {
    super(n);
  }

  static AspForStmt parse(Scanner s)
  {
    enterParser("For statement");
    AspForStmt afs = new AspForStmt(s.curLineNum());
    // skip() gir feilmelding om det er feil token, ellers går den videre
    skip(s, forToken);
    afs.var = AspName.parse(s);
    skip(s, inToken);
    afs.test = AspExpr.parse(s);
    skip(s, colonToken);
    afs.body = AspSuite.parse(s);

    leaveParser("For statement");
    return afs;
  }

  @Override
  void prettyPrint()
  {
    prettyWrite("for ");
    var.prettyPrint();
    prettyWrite(" in ");
    test.prettyPrint();
    prettyWrite(":");
    body.prettyPrint();
    prettyWriteLn();
  }

  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue{
    RuntimeValue testList = test.eval(curScope);

    if(testList instanceof RuntimeListValue){
      ArrayList<RuntimeValue> list = testList.getElements(this);

      for(int i=0; i<list.size()-1; i++){
        trace("for #" + (i+1) + ": " + var.nameVal + " = " + list.get(i).showInfo());
        curScope.assign(var.nameVal, list.get(i));
        testList = test.eval(curScope);
        body.eval(curScope);
      }

    }else if(testList instanceof RuntimeStringValue){
      String text = testList.getStringValue("Subscription", this);
      for(int i=0; i<text.length()-1; i++){
        trace("for #" + (i+1) + ": " + var.nameVal + " = " + text.charAt(i));
        curScope.assign(var.nameVal, new RuntimeStringValue(""+text.charAt(i)));
      }
      }else{
      RuntimeValue.runtimeError("forStmt - Expression is not itereable", this);
    }
    return testList;
  }
}
