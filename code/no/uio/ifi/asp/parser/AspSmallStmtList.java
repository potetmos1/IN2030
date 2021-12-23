
// Denne klassen lagrer en eller flere small statements i datastrukturen
// og kjører dem i samme rekkefølge.

package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.scanner.Scanner;
import static no.uio.ifi.asp.scanner.TokenKind.*;
import no.uio.ifi.asp.runtime.*;

import java.util.ArrayList;

public class AspSmallStmtList extends AspStmt
{
  ArrayList <AspSmallStmt> contents;

  AspSmallStmtList(int n)
  {
    super(n);
  }

  static AspSmallStmtList parse(Scanner s)
  {
    enterParser("Small statement list");
    AspSmallStmtList asl = new AspSmallStmtList(s.curLineNum());
    asl.contents = new ArrayList <> ();

    asl.contents.add(AspSmallStmt.parse(s));
    while (s.curToken().kind == semicolonToken)
    {
      skip(s, semicolonToken);
      if (s.curToken().kind == newLineToken)
      {
        break;
      }
      asl.contents.add(AspSmallStmt.parse(s));
    }

    skip(s, newLineToken);
    leaveParser("Small statement list");
    return asl;
  }

  @Override
  void prettyPrint()
  {
    int counter = 1;
    for(AspSmallStmt small: contents)
    {
      if (counter > 1)
      {
        prettyWrite("; ");
      }
      small.prettyPrint();
      counter++;
    }
    prettyWriteLn();
  }

  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue{
    RuntimeValue returnVal = null;

    for(AspSmallStmt small: contents)
    {
      returnVal = small.eval(curScope);
    }
    return returnVal;
  }
}
