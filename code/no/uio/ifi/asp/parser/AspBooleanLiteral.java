package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.Scanner;
import static no.uio.ifi.asp.scanner.TokenKind.*;

//Boolean class for true and false values
public class AspBooleanLiteral extends AspAtom{
    Boolean value;

    AspBooleanLiteral(int num){
        super(num);
    }

    static AspBooleanLiteral parse(Scanner s){
        enterParser("Boolean literal");
        AspBooleanLiteral bool = new AspBooleanLiteral(s.curLineNum());
        if(s.curToken().kind.toString().equals("True")){ //checks to see if the token is true
            bool.value = true;
        }else{
            bool.value = false;
        }
        
        s.readNextToken();
        leaveParser("Boolean literal");
        return bool;
    }

    @Override
    public void prettyPrint(){
        if(value){
            prettyWrite("True");
        }else{
            prettyWrite("False");
        }
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue{
        return new RuntimeBoolValue(value);
    }
}