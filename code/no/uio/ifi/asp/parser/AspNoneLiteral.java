package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;
import no.uio.ifi.asp.scanner.Scanner;

//None class for null values in Asp/Python
//Nothing to see here
public class AspNoneLiteral extends AspAtom{

    AspNoneLiteral(int num){
        super(num);
    }

    static AspNoneLiteral parse(Scanner s){
        enterParser("None");
        AspNoneLiteral none = new AspNoneLiteral(s.curLineNum());
        s.readNextToken();
        leaveParser("None");
        return none;
    }

    @Override
    public void prettyPrint(){
        prettyWrite("None");
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue{
        return new RuntimeNoneValue();
    }
}
