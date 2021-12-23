package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import no.uio.ifi.asp.scanner.TokenKind.*;

import java.util.ArrayList;

//class for factors, needing prefix, operator and primary
public class AspFactor extends AspSyntax{
    ArrayList<AspFactorPrefix> facpre = new ArrayList<>();
    ArrayList<AspFactorOpr> facopr = new ArrayList<>();
    ArrayList<AspPrimary> prims = new ArrayList<>();

    AspFactor(int num){
        super(num);
    }

    public static AspFactor parse(Scanner s){
        AspFactor aspFac = new AspFactor(s.curLineNum());
        Main.log.enterParser("AspFactor");

       while(true){

            if(s.isFactorPrefix()){
               aspFac.facpre.add(AspFactorPrefix.parse(s));
           }else{
                aspFac.facpre.add(null);
           }

           aspFac.prims.add(AspPrimary.parse(s));

           if(s.isFactorOpr()){
                aspFac.facopr.add(AspFactorOpr.parse(s));
           }else break;
       }

        Main.log.leaveParser("AspFactor");
        return aspFac;
    }

    @Override
    public void prettyPrint(){
        for(int i=0; i<prims.size(); i++){
            if(facpre.get(i) != null){
                facpre.get(i).prettyPrint();
            }

            prims.get(i).prettyPrint();

            if(facopr.size() > i){
                facopr.get(i).prettyPrint();
            }
        }
    }

    @Override //checks if legal opration for factor, evals or throws error
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue runtimeValue = prims.get(0).eval(curScope);
        if (facpre.size() != 0) {
            if (facpre.get(0) != null) {
                TokenKind tokenKind = facpre.get(0).t.kind;
                switch (tokenKind) {
                    case plusToken:
                        runtimeValue = runtimeValue.evalPositive(this);
                        break;
                    case minusToken:
                        runtimeValue = runtimeValue.evalNegate(this);
                        break;
                    default:
                        Main.panic("Illegal factor operator" + tokenKind);
                }
            }
        }
            //own loop for plus and minus
            for (int i = 1; i < prims.size(); i++) {
                TokenKind tokenKind = facopr.get(i-1).t.kind;
                RuntimeValue next = prims.get(i).eval(curScope);
                if (facpre.get(i) != null) {
                    TokenKind nextKind = facpre.get(i).t.kind;
                    switch (nextKind) {
                        case plusToken:
                            next = next.evalPositive(this);
                            break;
                        case minusToken:
                            next = next.evalNegate(this);
                            break;
                        default:
                            Main.panic("Illegal factor operator" + tokenKind);
                    }
                }
                //check what kind of operation is needed
                switch (tokenKind) {
                    case percentToken:
                        runtimeValue = runtimeValue.evalModulo(next, this);
                        break;
                    case slashToken:
                        runtimeValue = runtimeValue.evalDivide(next, this);
                        break;
                    case doubleSlashToken:
                        runtimeValue = runtimeValue.evalIntDivide(next, this);
                        break;
                    case astToken:
                        runtimeValue = runtimeValue.evalMultiply(next, this);
                        break;
                    default:
                        Main.panic("illefal factor operator " + tokenKind);
                }

            }
        return runtimeValue;
    }
}
