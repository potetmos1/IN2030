package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.Scanner;

//Float class, using doubble for values
public class AspFloatLiteral extends AspAtom{
    double value;

    AspFloatLiteral(int num){
        super(num);
    }

    static AspFloatLiteral parse(Scanner s){
        enterParser("float");
        AspFloatLiteral numFloat = new AspFloatLiteral(s.curLineNum());
        numFloat.value = s.curToken().floatLit;
        s.readNextToken();
        leaveParser("float");
        return numFloat;
    }

    @Override
    public void prettyPrint(){
        prettyWrite(""+value);
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue{
        return new RuntimeFloatValue(value);
    }
}
