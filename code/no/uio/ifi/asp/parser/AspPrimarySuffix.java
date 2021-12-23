
// Denne abstrakte klassen skiller mellom AspArguments
// og AspSubscription som angir index.

package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.scanner.Scanner;
import static no.uio.ifi.asp.scanner.TokenKind.*;
import no.uio.ifi.asp.runtime.*;

import java.util.ArrayList;

public abstract class AspPrimarySuffix extends AspSyntax
{
  AspPrimarySuffix(int n)
  {
    super(n);
  }

  static AspPrimarySuffix parse(Scanner s)
  {
    enterParser("Primary suffix");
    AspPrimarySuffix aps = null;

    if (s.curToken().kind == leftParToken)
    {
      aps = AspArguments.parse(s);
    }
    else if (s.curToken().kind == leftBracketToken)
    {
      aps = AspSubscription.parse(s);
    }
    else
    {
      parserError("Expected [ or (", s.curLineNum());
    }


    leaveParser("Primary suffix");
    return aps;
  }

}
