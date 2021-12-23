package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;

//Factor operators needed for AspFactor
public class AspFactorOpr extends AspSyntax{
    Token t;

    AspFactorOpr(int num){
        super(num);
    }

    public static AspFactorOpr parse(Scanner s){
        enterParser("Factor operator");
        AspFactorOpr factOpr = new AspFactorOpr(s.curLineNum());

        factOpr.t = s.curToken();
        skip(s, s.curToken().kind);

        leaveParser("Factor operator");
        return factOpr;
    }

    @Override
    public void prettyPrint(){
        prettyWrite(" " + t.toString() + " ");
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue{
        return null; //required by the compiler
    }
}
