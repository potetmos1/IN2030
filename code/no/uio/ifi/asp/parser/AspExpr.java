// © 2021 Dag Langmyhr, Institutt for informatikk, Universitetet i Oslo

package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

//Expression class for handling expressions
public class AspExpr extends AspSyntax {
    ArrayList<AspAndTest> andTests = new ArrayList<>();

    AspExpr(int n) {
	     super(n);
    }


    public static AspExpr parse(Scanner s)
    {
    	enterParser("expr");
      AspExpr aspExpr = new AspExpr(s.curLineNum());
      while(true){
          aspExpr.andTests.add(AspAndTest.parse(s));
          if(s.curToken().kind != TokenKind.orToken){ //nothing more to check
              break;
          }else{
              skip(s, TokenKind.orToken);
          }
      }

    	leaveParser("expr");
    	return aspExpr;
    }


    @Override
    public void prettyPrint() {
        int count = 0;
        for(AspAndTest t: andTests){
            if(count > 0){
                prettyWrite(" or ");
            }
            t.prettyPrint();
            count++;
        }
    }

    @Override
    //Goes over the andTests and evals them, then returning result
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue returnValue = andTests.get(0).eval(curScope);
        for(int i=1; i<andTests.size(); i++){
            if(returnValue.getBoolValue("or operand", this)){
                return returnValue;
            }
            returnValue = andTests.get(i).eval(curScope);
        }
        return returnValue;
    }
}
