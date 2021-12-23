
// Klassen AspSuite lagrer innmaten i sammensatte uttrykk i datastrukturen
// og kjører dem.

package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.scanner.Scanner;
import static no.uio.ifi.asp.scanner.TokenKind.*;
import no.uio.ifi.asp.runtime.*;

import java.util.ArrayList;

public class AspSuite extends AspSyntax
{
  AspSmallStmtList body;
  ArrayList <AspStmt> contents = null;

  AspSuite(int n)
  {
    super(n);
  }

  static AspSuite parse(Scanner s)
  {
    enterParser("Suite expression");
    AspSuite asu = new AspSuite(s.curLineNum());

    asu.contents = new ArrayList <> ();
    if (s.curToken().kind == newLineToken)
    {
      skip(s, newLineToken);
      skip(s, indentToken);
      while (s.curToken().kind != dedentToken)
      {
        asu.contents.add(AspStmt.parse(s));
      }
      skip(s, dedentToken);
    }
    else
    {
      asu.body = AspSmallStmtList.parse(s);
    }

    leaveParser("Suite expression");
    return asu;
  }

  @Override
  void prettyPrint(){
    if(body == null){
      prettyWriteLn();
      prettyIndent();

      for(AspStmt stmt: contents){
        stmt.prettyPrint();
      }
      prettyDedent();
    }else{
      body.prettyPrint();
    }
  }

  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue{
  RuntimeValue returnVal = null;

  if(body == null){
    for(AspStmt stmt: contents){
      returnVal = stmt.eval(curScope);
    }

  }else{
    returnVal = body.eval(curScope);
  }
  return returnVal;
  }
}
