
// Denne klassen lagrer primary-verdier, som har prioritet til å
// evalueres før multiplikasjon og divisjon. Den kalles innen
// aspFactor.

package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.scanner.Scanner;
import static no.uio.ifi.asp.scanner.TokenKind.*;
import no.uio.ifi.asp.runtime.*;

import java.util.ArrayList;

public class AspPrimary extends AspSyntax
{
  AspAtom header;
  ArrayList<AspPrimarySuffix> body;

  AspPrimary(int n)
  {
    super(n);
  }

  static AspPrimary parse(Scanner s)
  {
    enterParser("Primary");
    AspPrimary ap = new AspPrimary(s.curLineNum());

    ap.body = new ArrayList <> ();
    ap.header = AspAtom.parse(s);

    while (s.curToken().kind == leftParToken || s.curToken().kind == leftBracketToken)
    {
      ap.body.add(AspPrimarySuffix.parse(s));
    }


    leaveParser("Primary");
    return ap;
  }

  @Override
  void prettyPrint()
  {
    header.prettyPrint();
    for(AspPrimarySuffix ap: body){
      ap.prettyPrint();
    }
  }

  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue{
    RuntimeValue returnValue = header.eval(curScope);
    ArrayList<RuntimeValue> args;

    // Med lister og lignende er primary suffix indeksen.
    for(AspPrimarySuffix suf : body){
      if(returnValue instanceof RuntimeDictValue || returnValue instanceof RuntimeListValue
          || returnValue instanceof RuntimeStringValue){
          returnValue = returnValue.evalSubscription(suf.eval(curScope), this);
      }else if(suf instanceof AspArguments){
        args = suf.eval(curScope).getElements(this);
        returnValue = returnValue.evalFuncCall(args, this);
      }
    }
    return returnValue;
  }
}
