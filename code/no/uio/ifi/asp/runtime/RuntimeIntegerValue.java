package no.uio.ifi.asp.runtime;

import java.util.ArrayList;
import java.lang.Math;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;

//class for handling integer values at runtime
public class RuntimeIntegerValue extends RuntimeValue{
    long numValue;

    public RuntimeIntegerValue(long v){
        numValue = v;
    }

    @Override
    public String toString(){
        return String.valueOf(numValue);
    }

    @Override
    public String typeName(){
        return "Integer";
    }

    @Override
    public long getIntValue(String what, AspSyntax where){
        return numValue;
    }

    @Override
    public boolean getBoolValue(String what, AspSyntax where){
        return(numValue != 0);
    }

    @Override
    public String getStringValue(String what, AspSyntax where){
        return String.valueOf(numValue);
    }

    @Override
    public RuntimeValue evalSubtract(RuntimeValue v, AspSyntax where){
        if (v instanceof RuntimeFloatValue){
            RuntimeFloatValue other = (RuntimeFloatValue) v;
            return new RuntimeFloatValue((double) (numValue - other.getFloatValue(" - Operator", where)));
        }

        if(v instanceof RuntimeIntegerValue){
            return new RuntimeIntegerValue(numValue - v.getIntValue(" - Operator", where));
        }

        runtimeError("'-' undefined for " + typeName() + " and " + v.typeName() + "!", where);
        return null;
    }

    @Override
    public RuntimeValue evalAdd(RuntimeValue v, AspSyntax where){
        if(v instanceof RuntimeFloatValue){
            return new RuntimeFloatValue((numValue + v.getFloatValue("+ Operator",where)));
        }
        if(v instanceof RuntimeIntegerValue){
            return new RuntimeIntegerValue(numValue + v.getIntValue("+ Operator",where));
        }
        runtimeError("'+' undefined for " + typeName() + " and " + v.typeName() + "!", where);
        return null;
    }

    @Override
    public RuntimeValue evalDivide(RuntimeValue v, AspSyntax where){
        if(v instanceof RuntimeFloatValue){
            return new RuntimeFloatValue((double) numValue / v.getFloatValue("/ Operator", where));
        }
        if(v instanceof RuntimeIntegerValue){
            return new RuntimeFloatValue((double) numValue / v.getIntValue("/ Operator", where));
        }
        runtimeError("'/' undefined for " + typeName() + " and " + v.typeName() + "!", where);
        return null;
    }

    @Override
    public RuntimeValue evalIntDivide(RuntimeValue v, AspSyntax where){
        if(v instanceof RuntimeFloatValue){
            return new RuntimeFloatValue((double) (Math.floor(numValue / v.getFloatValue("// Operator", where))));
        }
        if(v instanceof RuntimeIntegerValue){
            return new RuntimeIntegerValue((long) (Math.floor(numValue / v.getIntValue("// Operator", where))));
        }
        runtimeError("'//' undefined for " + typeName() + " and " + v.typeName() + "!", where);
        return null;
    }


    @Override
    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where){
        if(v instanceof RuntimeIntegerValue){
            return new RuntimeBoolValue(numValue == v.getIntValue("== Operator", where));
        }
        if(v instanceof RuntimeFloatValue){
            RuntimeFloatValue floatVal = new RuntimeFloatValue((double) numValue);
            return new RuntimeBoolValue(floatVal.getFloatValue("== Operator", where) == v.getFloatValue("== Operator", where));
        }
        runtimeError("'==' undefined for " + typeName() + " and " + v.typeName() + "!", where);
        return null;

    }

    @Override
    public RuntimeValue evalGreater(RuntimeValue v, AspSyntax where){
        if(v instanceof RuntimeIntegerValue){
            return new RuntimeBoolValue(numValue > v.getIntValue("> Operator", where));
        }
        if(v instanceof RuntimeFloatValue){
            return new RuntimeBoolValue(numValue > v.getFloatValue("> Operator", where));
        }
        runtimeError("'>' undefined for " + typeName() + " and " + v.typeName() + "!", where);
        return null;
    }

    @Override
    public RuntimeValue evalGreaterEqual(RuntimeValue v, AspSyntax where){
        if(v instanceof RuntimeIntegerValue){
            return new RuntimeBoolValue(numValue >= v.getIntValue(">= Operator", where));
        }
        if(v instanceof RuntimeFloatValue){
            return new RuntimeBoolValue(numValue >= v.getFloatValue(">= Operator", where));
        }
        runtimeError("'>=' undefined for " + typeName() + " and " + v.typeName() + "!", where);
        return null;
    }

    @Override
    public RuntimeValue evalLess(RuntimeValue v, AspSyntax where){
        if(v instanceof RuntimeIntegerValue){
            return new RuntimeBoolValue(numValue < v.getIntValue("< Operator", where));
        }
        if(v instanceof RuntimeFloatValue){
            return new RuntimeBoolValue(numValue < v.getFloatValue("< Operator", where));
        }
        runtimeError("'<' undefined for " + typeName() + " and " + v.typeName() + "!", where);
        return null;
    }

    @Override
    public RuntimeValue evalLessEqual(RuntimeValue v, AspSyntax where){
        if(v instanceof RuntimeIntegerValue){
            return new RuntimeBoolValue(numValue <= v.getIntValue("<= Operator", where));
        }
        if(v instanceof RuntimeFloatValue){
            return new RuntimeBoolValue(numValue <= v.getFloatValue("<= Operator", where));
        }
        runtimeError("'<=' undefined for " + typeName() + " and " + v.typeName() + "!", where);
        return null;
    }

    @Override
    //using real modulo instead of remainder like in java
    public RuntimeValue evalModulo(RuntimeValue v, AspSyntax where){
        if(v instanceof RuntimeIntegerValue){
            long next = v.getIntValue("% Operator", where);
            long newValue = Math.floorMod(numValue, next);
            return new RuntimeIntegerValue(newValue);
        }
        if(v instanceof RuntimeFloatValue){
            double next = v.getFloatValue("% Operator", where);
            double newValue = numValue - next*Math.floor(numValue/next);
            return new RuntimeFloatValue(newValue);
        }
        runtimeError("'%' undefined for " + typeName() + " and " + v.typeName() + "!", where);
        return null;
    }

    @Override
    public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where){
        if(v instanceof RuntimeFloatValue){
            return new RuntimeFloatValue((double)(numValue * v.getFloatValue("* Operator", where)));
        }
        if(v instanceof RuntimeIntegerValue){
            return new RuntimeIntegerValue(numValue * v.getIntValue("* Operator", where));
        }
        runtimeError("'*' undefined for " + typeName() + " and " + v.typeName() + "!", where);
        return null;
    }

    @Override
    public RuntimeValue evalNegate(AspSyntax where){
        return new RuntimeIntegerValue(-numValue);
    }

    @Override
    public RuntimeValue evalNot(AspSyntax where){
        if(numValue == 0){
            return new RuntimeBoolValue(true);
        }
        return new RuntimeBoolValue(false);
    }

    @Override
    public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where){
        if(v instanceof RuntimeIntegerValue){
            return new RuntimeBoolValue(numValue != v.getIntValue("!= Operator", where));
        }
        if(v instanceof RuntimeFloatValue){
            return new RuntimeBoolValue(numValue != v.getFloatValue("!= Operator", where));
        }
        runtimeError("'!=' undefined for " + typeName() + " and " + v.typeName() + "!", where);
        return null;
    }

    @Override
    public RuntimeValue evalPositive(AspSyntax where){
        return new RuntimeIntegerValue(numValue);
    }
}
