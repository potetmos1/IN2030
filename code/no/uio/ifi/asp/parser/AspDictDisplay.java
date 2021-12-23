package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import no.uio.ifi.asp.scanner.TokenKind.*;

import java.util.ArrayList;

//Dict class for dictionary values
public class AspDictDisplay extends AspAtom{
    ArrayList<AspStringLiteral> dictKeys = new ArrayList<>();
    ArrayList<AspExpr> dictValues = new ArrayList<>();

    AspDictDisplay(int num){
        super(num);
    }

    static AspDictDisplay parse(Scanner s){
        Main.log.enterParser("dict");
        AspDictDisplay dict = new AspDictDisplay(s.curLineNum());
        skip(s, TokenKind.leftBraceToken);

        //keeps on parsing until rightBraceToken
        while(s.curToken().kind != TokenKind.rightBraceToken){
            dict.dictKeys.add(AspStringLiteral.parse(s));
            skip(s, TokenKind.colonToken);

            //skips over comma tokens since they are not needed
            dict.dictValues.add(AspExpr.parse(s));
            if(s.curToken().kind == TokenKind.commaToken){
                skip(s, TokenKind.commaToken);
            }
        }

        skip(s, TokenKind.rightBraceToken);
        Main.log.leaveParser("dict");
        return dict;
    }

    @Override
    public void prettyPrint(){
        prettyWrite("{");

        for(int i=0; i< dictKeys.size(); i++){
            dictKeys.get(i).prettyPrint();
            prettyWrite(": ");
            dictValues.get(i).prettyPrint();

            if(i < dictKeys.size()-1){
                prettyWrite(", ");
            }
        }
        prettyWrite("}");
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue{
        RuntimeDictValue returnDict = new RuntimeDictValue(new ArrayList <RuntimeValue>(),
                                                           new ArrayList <RuntimeValue>());

        int i = 0; //using indexing to get both keys and values in iteration
        for(AspStringLiteral key: dictKeys){
            returnDict.evalAssignElem(key.eval(curScope), dictValues.get(i).eval(curScope), this);
            i++;
        }
        return returnDict;
    }
}
