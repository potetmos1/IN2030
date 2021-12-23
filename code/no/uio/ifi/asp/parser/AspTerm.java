package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;
import no.uio.ifi.asp.scanner.*;

import java.util.ArrayList;

//class for terms using factors and operators to return result
public class AspTerm extends AspSyntax{
    ArrayList<AspFactor> factors = new ArrayList<>();
    ArrayList<AspTermOpr> termOps = new ArrayList<>();

    AspTerm(int num){
        super(num);
    }

    public static AspTerm parse(Scanner s){
        enterParser("term");
        AspTerm term = new AspTerm(s.curLineNum());

        //continues while there are factorprefixes
        while(true){
            term.factors.add(AspFactor.parse(s));

            if(s.isFactorPrefix()){
                term.termOps.add(AspTermOpr.parse(s));
            }else break;
        }
        leaveParser("term");
        return term;

    }

    @Override
    //printing factors and operators if there are any
    public void prettyPrint(){
        int count = 0;
        for(AspFactor fac: factors){
            fac.prettyPrint();

            if(count < termOps.size()){
                if(termOps.get(count) != null){
                    AspTermOpr termOp = termOps.get(count);
                    termOp.prettyPrint();
                }
            }
            count++;
        }
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue{
        RuntimeValue returnValue = factors.get(0).eval(curScope);

        //evals as plus or minus depending on operator
        for(int i=1; i<factors.size(); i++){
            TokenKind kind = termOps.get(i-1).k.kind;
            if(kind == minusToken){
                returnValue = returnValue.evalSubtract(factors.get(i).eval(curScope), this);
            }else{
                returnValue = returnValue.evalAdd(factors.get(i).eval(curScope), this);
            }
        }

        return returnValue;
    }
}
