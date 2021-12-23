package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind.*;
import no.uio.ifi.asp.scanner.TokenKind;

//Inner expression class for expressions inside other expressions
public class AspInnerExpr extends AspAtom{
    AspExpr expression;

    AspInnerExpr(int num){
        super(num);
    }

    public static AspInnerExpr parse(Scanner s){
        enterParser("inner expression");
        AspInnerExpr expr = new AspInnerExpr(s.curLineNum());
        
        //skips on '(' and ')' since they are not needed
        skip(s, TokenKind.leftParToken);
        expr.expression = AspExpr.parse(s);
        skip(s, TokenKind.rightParToken);

        leaveParser("inner expression");
        return expr;
    }

    @Override
    public void prettyPrint(){
        prettyWrite("(");
        expression.prettyPrint();
        prettyWrite(")");
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue{
        return expression.eval(curScope);
    }
}
