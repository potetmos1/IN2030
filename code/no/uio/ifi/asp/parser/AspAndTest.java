package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.Scanner;
import java.util.ArrayList;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspAndTest extends AspSyntax{
    ArrayList<AspNotTest> tests = new ArrayList<>();

    AspAndTest(int num){
        super(num);
    }

    public static AspAndTest parse(Scanner s){
        enterParser("and test");
        AspAndTest andTest = new AspAndTest(s.curLineNum());

        while(true){
            andTest.tests.add(AspNotTest.parse(s));

            if(s.curToken().kind != andToken) break;
            skip(s, andToken);
        }

        leaveParser("and test");
        return andTest;
    }

    @Override
    public void prettyPrint(){
        int count = 0;

        for(AspNotTest t: tests){
            if(count > 0){
                Main.log.prettyWrite(" and ");
            }
            t.prettyPrint();
            count++;
        }
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue
    {
    //-- Must be changed in part 4:
      RuntimeValue v = tests.get(0).eval(curScope);;
      for (int i = 1; i < tests.size(); ++i)
      {
        if (! v.getBoolValue("and operand",this))
        {
          return v;
        }
        v = tests.get(i).eval(curScope);
      }
      return v;
    }
}
