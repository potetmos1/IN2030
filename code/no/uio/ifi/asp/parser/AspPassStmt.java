
// Denne klassen setter pass statements inn i datastrukturen, de endrer ikke
// på noe når de kjøres.

package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.scanner.Scanner;
import static no.uio.ifi.asp.scanner.TokenKind.*;
import no.uio.ifi.asp.runtime.*;

import java.util.ArrayList;

public class AspPassStmt extends AspSmallStmt
{

  AspPassStmt(int n)
  {
    super(n);
  }

  static AspPassStmt parse(Scanner s)
  {
    enterParser("Pass statement");
    AspPassStmt aps = new AspPassStmt(s.curLineNum());

    skip(s, passToken);

    leaveParser("Pass statement");
    return aps;
  }

  @Override
  void prettyPrint()
  {
    prettyWrite("pass");
  }

  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue
  {
    trace("pass");
    return null;
  }
}
