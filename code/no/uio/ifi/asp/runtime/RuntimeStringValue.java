// Strings er implementert som Java strings.

package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeStringValue extends RuntimeValue
{
  String stringValue;

  public RuntimeStringValue(String v)
  {
    stringValue = v;
  }

  @Override
  String typeName()
  {
    return "String";
  }


  @Override
  public String toString()
  {
    return ("\""+stringValue+"\"");
  }


  @Override
  public boolean getBoolValue(String what, AspSyntax where)
  {
    if (stringValue == "")
    {
      return false;
    }
    return true;
  }

  @Override
  public String getStringValue(String what, AspSyntax where)
  {
    return stringValue;
  }

  @Override
  public long getIntValue(String what, AspSyntax where){
      return (long) Integer.parseInt(stringValue);
  }

  @Override
  public double getFloatValue(String what, AspSyntax where)
  {
    return Double.parseDouble(stringValue);
  }

  @Override
  public RuntimeValue evalAdd(RuntimeValue v, AspSyntax where)
  {
    if (v instanceof RuntimeStringValue)
    {
      String newString = stringValue + v.getStringValue("String", where);
      return new RuntimeStringValue(newString);
    }
    runtimeError("'+' undefined for " + typeName() + " and " + v.typeName() + "!", where);
    return null;
  }

  @Override
  public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where)
  {
    if (v instanceof RuntimeIntegerValue)
    {
      // Lager en tom array med gitt lengde og setter inn stringen på hver plass
      String newString = new String
        (new char[(int)v.getIntValue("int", where)])
        .replace("\0", stringValue);
      return new RuntimeStringValue(newString);
    }
    runtimeError("'*' undefined for " + typeName() + " and " + v.typeName() + "!", where);
    return null;
  }

  @Override
  public RuntimeValue evalLen(AspSyntax where)
  {
    return new RuntimeIntegerValue(stringValue.length());
  }

  @Override
  public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where)
  {
    if (v instanceof RuntimeStringValue)
    {
      if (stringValue.equals(v.getStringValue("String", where)))
      {
        return new RuntimeBoolValue(true);
      }
      return new RuntimeBoolValue(false);
    }
    runtimeError("'==' undefined for " + typeName() + " and " + v.typeName() + "!", where);
    return null;
  }

  @Override
  public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where)
  {
    if (v instanceof RuntimeStringValue)
    {
      if (stringValue.equals(v.getStringValue("String", where)))
      {
        return new RuntimeBoolValue(false);
      }
      return new RuntimeBoolValue(true);
    }
    runtimeError("'!=' undefined for " + typeName() + " and " + v.typeName() + "!", where);
    return null;
  }

  @Override
  public RuntimeValue evalNot(AspSyntax where)
  {
    return new RuntimeBoolValue(stringValue == "");
  }

  @Override
  public RuntimeValue evalLess(RuntimeValue v, AspSyntax where)
  {
    if (v instanceof RuntimeStringValue)
    {
      if (stringValue.length() < v.getStringValue("String", where).length())
      {
        return new RuntimeBoolValue(true);
      }
      return new RuntimeBoolValue(false);
    }
    runtimeError("'<' undefined for " + typeName() + " and " + v.typeName() + "!", where);
    return null;
  }

  @Override
  public RuntimeValue evalLessEqual(RuntimeValue v, AspSyntax where)
  {
    if (v instanceof RuntimeStringValue)
    {
      if (stringValue.length() <= v.getStringValue("String", where).length())
      {
        return new RuntimeBoolValue(true);
      }
      return new RuntimeBoolValue(false);
    }
    runtimeError("'<=' undefined for " + typeName() + " and " + v.typeName() + "!", where);
    return null;
  }

  @Override
  public RuntimeValue evalGreater(RuntimeValue v, AspSyntax where)
  {
    if (v instanceof RuntimeStringValue)
    {
      if (stringValue.length() > v.getStringValue("String", where).length())
      {
        return new RuntimeBoolValue(true);
      }
      return new RuntimeBoolValue(false);
    }
    runtimeError("'>' undefined for " + typeName() + " and " + v.typeName() + "!", where);
    return null;
  }

  @Override
  public RuntimeValue evalGreaterEqual(RuntimeValue v, AspSyntax where)
  {
    if (v instanceof RuntimeStringValue)
    {
      if (stringValue.length() >= v.getStringValue("String", where).length())
      {
        return new RuntimeBoolValue(true);
      }
      return new RuntimeBoolValue(false);
    }
    runtimeError("'>=' undefined for " + typeName() + " and " + v.typeName() + "!", where);
    return null;
  }

  @Override
  public RuntimeValue evalSubscription(RuntimeValue v, AspSyntax where)
  {
    if (v instanceof RuntimeIntegerValue)
    {
      int index = (int) v.getIntValue("Int", where);
      if (index > stringValue.length()-1)
      {
          runtimeError("TypeError: Index out of bounds for " + typeName() + "!", where);
      }
      return new RuntimeStringValue(""+stringValue.charAt(index));
    }
    runtimeError("Subscription undefined for " + typeName() + " and " + v.typeName() + "!", where);
    return null;
  }
}
