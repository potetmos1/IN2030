package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

import java.util.ArrayList;

//Comparison class for comparing values and expressions
public class AspComparison extends AspSyntax{
    ArrayList<AspTerm> terms = new ArrayList<>();
    ArrayList<AspCompOpr> compOps = new ArrayList<>();

    AspComparison(int num){
        super(num);
    }

    public static AspComparison parse(Scanner s){
        enterParser("comparison");
        AspComparison comp = new AspComparison(s.curLineNum());

        addCompOps: //used to break out of the loop
        while(true){
            comp.terms.add(AspTerm.parse(s));

            switch(s.curToken().kind){
                case lessToken:
                case greaterToken:
                case doubleEqualToken:
                case greaterEqualToken:
                case lessEqualToken:
                case notEqualToken:
                    comp.compOps.add(AspCompOpr.parse(s));
                    break;

                default:
                break addCompOps;
            }
        }

        leaveParser("comparison");
        return comp;
    }

    @Override
    public void prettyPrint(){
        int count = 0;
        for(AspTerm t: terms){
            t.prettyPrint();
            if(count < compOps.size()){
                compOps.get(count++).prettyPrint();
            }
        }
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {

        //cheks to find type of comparitor, if not found, throws error
		RuntimeValue returnValue = terms.get(0).eval(curScope);
		for (int i = 1; i < terms.size(); i++){
			returnValue  = terms.get(i-1).eval(curScope);
			TokenKind t = compOps.get(i-1).k;
			if(t == TokenKind.lessToken){
				returnValue  = returnValue.evalLess(terms.get(i).eval(curScope), this);
			}
			else if(t == TokenKind.greaterToken){
				returnValue = returnValue.evalGreater(terms.get(i).eval(curScope), this);
			}
			else if(t == TokenKind.doubleEqualToken){
				returnValue = returnValue.evalEqual(terms.get(i).eval(curScope), this);
			}
			else if(t == TokenKind.greaterEqualToken){
				returnValue = returnValue.evalGreaterEqual(terms.get(i).eval(curScope), this);
			}
			else if(t == TokenKind.lessEqualToken){
				returnValue = returnValue.evalLessEqual(terms.get(i).eval(curScope), this);
			}
			else if(t == TokenKind.notEqualToken){
				returnValue = returnValue.evalNotEqual(terms.get(i).eval(curScope), this);
			}
			else{
				Main.panic("Do not find comparison operator: " + t);
			}
			if(!returnValue.getBoolValue("comparison", this)){
				return returnValue;
			}
		}
		return returnValue;
	}
}
