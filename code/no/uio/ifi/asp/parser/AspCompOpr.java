package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind.*;
import no.uio.ifi.asp.scanner.TokenKind;

//Comparison operators used in the comparison class
public class AspCompOpr extends AspSyntax{
    TokenKind k;

    AspCompOpr(int num){
        super(num);
    }

    public static AspCompOpr parse(Scanner s){
        AspCompOpr compOpr = new AspCompOpr(s.curLineNum());
        enterParser("compOpr");

        compOpr.k = s.curToken().kind;
        skip(s, compOpr.k);

        leaveParser("compOpr");
        return compOpr;
    }

    @Override
    public void prettyPrint(){
        prettyWrite(" " + k.toString() + " ");
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue{
        return null; //required by the compiler
    }
}
