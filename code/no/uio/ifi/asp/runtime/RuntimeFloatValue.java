package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;

//class for floatValues at runtime
public class RuntimeFloatValue extends RuntimeValue{
    double numValue;

    public RuntimeFloatValue(double v){
        numValue = v;
    }

    @Override
    public String toString(){
        return String.valueOf(numValue);
    }

    @Override
    String typeName(){
        return "float";
    }

    @Override
    public double getFloatValue(String what, AspSyntax where){
        return numValue;
    }

    @Override
    public boolean getBoolValue(String what, AspSyntax where){
        return(numValue != 0.0000);
    }

    @Override
    public String getStringValue(String what, AspSyntax where){
        return this.toString();
    }

    @Override
    public RuntimeValue evalSubtract(RuntimeValue v, AspSyntax where){
        if (v instanceof RuntimeFloatValue){
            return new RuntimeFloatValue(numValue - v.getFloatValue(" - Operator", where));
        }
        if(v instanceof RuntimeIntegerValue){
            return new RuntimeFloatValue(numValue - v.getIntValue(" - Operator", where));
        }
        runtimeError("'-' undefined for " + typeName() + " and " + v.typeName() + "!", where);
        return null;
    }

    @Override
    public RuntimeValue evalAdd(RuntimeValue v, AspSyntax where){
        if(v instanceof RuntimeFloatValue){
            return new RuntimeFloatValue(numValue + v.getFloatValue("+ Operator",where));
        }
        if(v instanceof RuntimeIntegerValue){
            return new RuntimeFloatValue(numValue + v.getIntValue("+ Operator",where));
        }
        runtimeError("'+' undefined for " + typeName() + " and " + v.typeName() + "!", where);
        return null;
    }

    @Override
    public RuntimeValue evalDivide(RuntimeValue v, AspSyntax where){
        if(v instanceof RuntimeFloatValue){
            return new RuntimeFloatValue(numValue / v.getFloatValue("/ Operator", where));
        }
        if(v instanceof RuntimeIntegerValue){
            return new RuntimeFloatValue(numValue / v.getIntValue("/ Operator", where));
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
            return new RuntimeFloatValue((double) (Math.floor(numValue / v.getIntValue("// Operator", where))));
        }
        runtimeError("'//' undefined for " + typeName() + " and " + v.typeName() + "!", where);
        return null;
    }

    @Override
    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where){
        if(v instanceof RuntimeFloatValue){
            return new RuntimeBoolValue(numValue == v.getFloatValue("== Operator", where));
        }
        if(v instanceof RuntimeIntegerValue){
            RuntimeFloatValue floatVal = new RuntimeFloatValue(v.getIntValue("== Operator", where));
            return new RuntimeBoolValue(numValue == floatVal.numValue);
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
            double newValue = numValue - next*Math.floor(numValue/next);
            return new RuntimeFloatValue(newValue);
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
            return new RuntimeFloatValue((numValue * v.getFloatValue("* Operator", where)));
        }
        if(v instanceof RuntimeIntegerValue){
            return new RuntimeFloatValue(numValue * v.getIntValue("* Operator", where));
        }
        runtimeError("'*' undefined for " + typeName() + " and " + v.typeName() + "!", where);
        return null;
    }

    @Override
    public RuntimeValue evalNegate(AspSyntax where){
        return new RuntimeFloatValue(-numValue);
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
        return new RuntimeFloatValue(numValue);
    }

}
