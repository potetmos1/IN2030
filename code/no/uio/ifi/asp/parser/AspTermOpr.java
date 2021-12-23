package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;
import no.uio.ifi.asp.scanner.Token;
import no.uio.ifi.asp.scanner.Scanner;

//term operator needed for terms
public class AspTermOpr extends AspSyntax{
    Token k;

    AspTermOpr(int num){
        super(num);
    }

    public static AspTermOpr parse(Scanner s){
        enterParser("term operator");
        AspTermOpr termOpr = new AspTermOpr(s.curLineNum());

        termOpr.k = s.curToken();
        skip(s, s.curToken().kind);

        leaveParser("term operator");
        return termOpr;

    }

    @Override
    public void prettyPrint(){
        prettyWrite(k.toString());
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue{
        return null; //required by the compiler
    }
}
