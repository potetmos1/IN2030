package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;
import no.uio.ifi.asp.scanner.Scanner;

//Integerclass for integer values
public class AspIntegerLiteral extends AspAtom{
    long value;

    AspIntegerLiteral(int num){
        super(num);
    }

    static AspIntegerLiteral parse(Scanner s){
        enterParser("Integer");
        AspIntegerLiteral numInt = new AspIntegerLiteral(s.curLineNum());
        numInt.value = s.curToken().integerLit;
        s.readNextToken();
        leaveParser("Integer");
        return numInt;
    }

    @Override
    public void prettyPrint(){
        prettyWrite(String.valueOf(value));
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue{
        return new RuntimeIntegerValue(value);
    }
}
