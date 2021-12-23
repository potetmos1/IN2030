
// Denne klassen lagrer if-statements i datastrukturen og kjører dem.
// elifs og else er implementert som egne if-statements.

package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.scanner.Scanner;
import static no.uio.ifi.asp.scanner.TokenKind.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.TokenKind.*;

import java.util.ArrayList;

public class AspIfStmt extends AspCompoundStmt
{
  AspExpr test;
  AspSuite body;
  ArrayList <AspIfStmt> elifs;
  AspIfStmt elseEnd;

  AspIfStmt(int n)
  {
    super(n);
  }

  static AspIfStmt parse(Scanner s)
  {
    enterParser("If statement");
    AspIfStmt ifst = new AspIfStmt(s.curLineNum());
    // skip() gir feilmelding om det er feil token, ellers går den videre
    skip(s, ifToken);
    ifst.test = AspExpr.parse(s);
    skip(s, colonToken);
    ifst.body = AspSuite.parse(s);

    // lagrer elifs som if-statements med egne tester
    ifst.elifs = new ArrayList<>();
    while (s.curToken().kind == elifToken)
    {
      AspIfStmt newif = new AspIfStmt(s.curLineNum());
      skip(s, elifToken);
      newif.test = AspExpr.parse(s);
      skip(s, colonToken);
      newif.body = AspSuite.parse(s);
      ifst.elifs.add(newif);
    }

    // lagrer else som et if-objekt med en tom test
    // (dette utnyttes senere)
    if (s.curToken().kind == elseToken)
    {
      AspIfStmt newif = new AspIfStmt(s.curLineNum());
      skip(s, elseToken);
      newif.test = null;
      skip(s, colonToken);
      newif.body = AspSuite.parse(s);
      ifst.elseEnd = newif;
    }

    leaveParser("If statement");

    return ifst;
  }

  @Override
  void prettyPrint()
  {
    prettyWrite("if ");
    test.prettyPrint();
    prettyWrite(": ");
    body.prettyPrint();

    for (AspIfStmt x: elifs)
    {
      prettyWrite("elif ");
      x.test.prettyPrint();
      prettyWrite(": ");
      x.body.prettyPrint();
    }

    if (elseEnd != null)
    {
      prettyWrite("else: ");
      elseEnd.body.prettyPrint();
    }
  }

  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue
  {
    // Eval funksjonen utnytter den rekursive implementasjonen.
    // Alle elifs kjøres slik at de vurderer sine egne tester.
    // Returnerer true/false for å bestemme om innsiden ble kjørt.

    // Sjekker om det er en else stmt, som alltid har tom test
    if (test == null)
    {
      trace("else: ");
      body.eval(curScope);
      return new RuntimeBoolValue(true);
    }

    // Sjekker if testen og returnerer true objekt om innsiden kjøres
    RuntimeValue newTest = test.eval(curScope);
    if (newTest.getBoolValue("if test", this))
    {
      trace("if True: ");
      body.eval(curScope);
      return new RuntimeBoolValue(true);
    }
    trace("if False");

    // kjører else if
    for (AspIfStmt elifTests: elifs)
    {
      trace("elif...");
      if (elifTests.eval(curScope).getBoolValue("elif test", this))
      {
        return new RuntimeBoolValue(true);
      }
    }

    // kjører else om den den når dit og den eksisterer
    if (elseEnd != null)
    {
      return elseEnd.eval(curScope);
    }

    // Dersom enden nås returnerer den et false-objekt for å markere
    // at testen i en elif er blitt false
    trace("if False");
    return new RuntimeBoolValue(false);
  }
}
