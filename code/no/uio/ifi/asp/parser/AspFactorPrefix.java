package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;

//class for factorprefixes, needed for factors
public class AspFactorPrefix extends AspSyntax{
    Token t;

    AspFactorPrefix(int num){
        super(num);
    }

    public static AspFactorPrefix parse(Scanner s){
        enterParser("Factor prefix");
        AspFactorPrefix factOpr = new AspFactorPrefix(s.curLineNum());

        factOpr.t = s.curToken();
        skip(s, s.curToken().kind);

        leaveParser("Factor prefix");
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
