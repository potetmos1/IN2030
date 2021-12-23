package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.Scanner;
import static no.uio.ifi.asp.scanner.TokenKind.*;

//Atom class for the ASP language used as building blocks
public abstract class AspAtom extends AspSyntax{
    
    AspAtom(int num){
        super(num);
    }

    static AspAtom parse(Scanner s){
        AspAtom atom;
        Main.log.enterParser("atom");

        //checks to see if any of the atomic values
        switch(s.curToken().kind){
            case trueToken:
                atom = AspBooleanLiteral.parse(s);
                break;
            case falseToken:
                atom = AspBooleanLiteral.parse(s);
                break;
            case floatToken:
                atom = AspFloatLiteral.parse(s);
                break;
            case integerToken:
                atom = AspIntegerLiteral.parse(s);
                break;
            case nameToken:
                atom = AspName.parse(s);
                break;
            case noneToken:
                atom = AspNoneLiteral.parse(s);
                break;
            case stringToken:
                atom = AspStringLiteral.parse(s);
                break;
            case leftBraceToken: 
                atom = AspDictDisplay.parse(s);
                break;
            case leftBracketToken: 
                atom = AspListDisplay.parse(s);
                break;
            case leftParToken: 
                atom = AspInnerExpr.parse(s);
                break;
            default: //returns error if no match
                parserError("Expected atom, but found a " + s.curToken().kind + ", on line " + s.curLineNum(), s.curLineNum());
                return null;
            }
            leaveParser("atom");
            return atom;
    }

    abstract void prettyPrint();
    abstract RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue; 
}