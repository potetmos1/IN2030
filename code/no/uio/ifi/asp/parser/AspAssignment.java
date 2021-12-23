
// Denne klassen oppretter datastruktur og kjører tilordninger.
// Filen bruker foreslåtte implementasjon fra lysbilder i IN2030.

package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.scanner.Scanner;
import static no.uio.ifi.asp.scanner.TokenKind.*;
import no.uio.ifi.asp.runtime.*;

import java.util.ArrayList;

public class AspAssignment extends AspSmallStmt
{
  AspName name;
  ArrayList <AspSubscription> extras;
  AspExpr body;

  AspAssignment(int n)
  {
    super(n);
  }

  static AspAssignment parse(Scanner s)
  {
    enterParser("Assignment");
    AspAssignment aas = new AspAssignment(s.curLineNum());
    aas.name = AspName.parse(s);

    aas.extras = new ArrayList <>();
    while (s.curToken().kind != equalToken)
    {
      aas.extras.add(AspSubscription.parse(s));
    }
    skip(s, equalToken);
    aas.body = AspExpr.parse(s);
    leaveParser("Assignment");

    return aas;
  }

  @Override
  void prettyPrint()
  {
    name.prettyPrint();
    for(AspSubscription x: extras)
    {
      x.prettyPrint();
    }
    prettyWrite(" = ");
    body.prettyPrint();
  }

  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue{
    RuntimeValue expression = body.eval(curScope);

    // Lagrer navnet og verdien den skal settes til i skopet
    if(extras.size() == 0){
      String id = name.nameVal;
      curScope.assign(id, expression);
      trace(id + " = " + expression.toString());
    }else{
      RuntimeValue v1 = name.eval(curScope);

      for(int i=0; i<extras.size()-1; i++){
        RuntimeValue v2 = extras.get(i).eval(curScope);
        v1 = v1.evalSubscription(v2, this);
      }

      AspSubscription lastSub = extras.get(extras.size()-1);
      RuntimeValue index = lastSub.eval(curScope);
      trace(name.nameVal + "[" + index.toString() + "] = " + expression.toString());
      v1.evalAssignElem(index, expression, this);
    }
    return null;
  }
}
