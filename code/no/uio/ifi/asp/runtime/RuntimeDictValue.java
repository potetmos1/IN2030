// Dictionaries er her implementert som to matchende arraylists.

package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;

import java.util.ArrayList;

public class RuntimeDictValue extends RuntimeValue
{
  // Liste av indexnavn
  ArrayList <RuntimeValue> keys;
  // liste av objekter
  ArrayList <RuntimeValue> values;

  public RuntimeDictValue(ArrayList <RuntimeValue> k, ArrayList <RuntimeValue> v)
  {
    keys = k;
    values = v;
  }

  @Override
  String typeName()
  {
    return "Dictionary";
  }

  @Override
  public String toString()
  {
    String printVal = "{";
    for(int i = 0; i < keys.size(); i++)
    {
      printVal += keys.get(i);
      printVal += ": ";
      printVal += values.get(i);
      if(i != keys.size()-1)
      {
        printVal += ", ";
      }
    }
    printVal += "}";
    return printVal;
  }

  @Override
  public boolean getBoolValue(String what, AspSyntax where)
  {
    if (keys.size() == 0)
    {
      return false;
    }
    return true;
  }

  @Override
  public String getStringValue(String what, AspSyntax where){
      return this.toString();
  }

  private void addElement(RuntimeValue key, RuntimeValue value)
  {
    for (int i = 0; i < keys.size(); i++)
    {
      // Oppdaterer verdien om index eksisterer
      if (key.getStringValue("String", null) == keys.get(i).getStringValue("String", null))
      {
        values.set(i, value);
        return;
      }
    }
    // Legger nye verdier til starten av listen
    keys.add(0, key);
    values.add(0, value);
  }

  @Override
  public RuntimeValue evalNot(AspSyntax where)
  {
    if (keys.size() == 0)
    {
      return new RuntimeBoolValue(true);
    }
    return new RuntimeBoolValue(false);
  }

  @Override
  public RuntimeValue evalSubscription(RuntimeValue inx, AspSyntax where)
  {
    if (!(inx instanceof RuntimeStringValue))
    {
      runtimeError("Index value " + inx + " of dictionary is not a string", where);
    }
    // Ser etter matchende index
    for(int i = 0;  i < keys.size(); i++)
    {
      if (inx.getStringValue("String", where)
      .equals(keys.get(i).getStringValue("String", where)))
      {
        return values.get(i);
      }
    }
    runtimeError("Index value " + inx + " of dictionary does not exist", where);
    return null;  // Required by the compiler!
  }

  @Override
  public void evalAssignElem(RuntimeValue inx, RuntimeValue val, AspSyntax where)
  {
    this.addElement(inx, val);
  }
}
