package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;
import no.uio.ifi.asp.scanner.Scanner;

//Name class for keeping track av variable names
public class AspName extends AspAtom{
    String nameVal;

    AspName(int num){
        super(num);
    }

    public static AspName parse(Scanner s){
        enterParser("Name");
        AspName name = new AspName(s.curLineNum());
        name.nameVal = s.curToken().name;
        skip(s, nameToken);
        leaveParser("Name");
        return name;
    }

    @Override
    public void prettyPrint(){
        prettyWrite(nameVal);
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue{
        return curScope.find(nameVal, this);
    }
}
