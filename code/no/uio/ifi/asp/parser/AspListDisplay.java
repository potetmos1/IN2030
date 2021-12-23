package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;
import no.uio.ifi.asp.scanner.Scanner;

import java.util.ArrayList;

//List class for list values, using ArrayList since it is most like the list in Python / Asp
public class AspListDisplay extends AspAtom{
    ArrayList<AspExpr> list = new ArrayList<>();

    AspListDisplay(int num){
        super(num);
    }

    public static AspListDisplay parse(Scanner s){
        enterParser("listdisplay");
        AspListDisplay listDisplay = new AspListDisplay(s.curLineNum());

        //skipping on brackets and commas since they are not needed in parsing
        skip(s, leftBracketToken);
        while(s.curToken().kind != rightBracketToken){
            listDisplay.list.add(AspExpr.parse(s));
            if(s.curToken().kind == commaToken){ 
                skip(s, commaToken);
            }
        }
        skip(s, rightBracketToken);
        leaveParser("listdisplay");
        return listDisplay;
    }

    @Override
    public void prettyPrint(){
        prettyWrite("[");
        for(int i=0; i <list.size(); i++){
            list.get(i).prettyPrint();
            if(list.size()-1 != i){
                prettyWrite(", ");
            }
        }
        prettyWrite("]");
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue{
        RuntimeListValue returnList = new RuntimeListValue(new ArrayList <RuntimeValue>());

        for(AspExpr ex: list){
            returnList.addElement(ex.eval(curScope));
        }

        return returnList;
    }
}
