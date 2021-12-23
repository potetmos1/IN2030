package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;

public class AspNotTest extends AspSyntax{
    AspComparison comparison;
    Boolean not = false;

    AspNotTest(int num) {
        super(num);
    }

    public static AspNotTest parse(Scanner s){
        enterParser("notTest");
        AspNotTest notTest = new AspNotTest(s.curLineNum());

        if(s.curToken().kind == TokenKind.notToken){
            skip(s, TokenKind.notToken);
            notTest.not = true;
        }
        notTest.comparison = AspComparison.parse(s);

        leaveParser("notTest");
        return notTest;
    }

    @Override
    public void prettyPrint(){
        if(not){
            prettyWrite(" not ");
        }
        comparison.prettyPrint();
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue
    {
      //-- Must be changed in part 4:
      RuntimeValue returnValue = comparison.eval(curScope);
      if (not)
      {
        return returnValue.evalNot(this);
      }
      else
      {
        return returnValue;
      }
    }
}
