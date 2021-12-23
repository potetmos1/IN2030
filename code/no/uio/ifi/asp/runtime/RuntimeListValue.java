package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;

import java.util.ArrayList;

//class for handlign lists at runtime
public class RuntimeListValue extends RuntimeValue{
    private ArrayList<RuntimeValue> elements;

    public RuntimeListValue(ArrayList<RuntimeValue> list){
        elements = list;
    }

    //hjelpemetode for å adde til listen
    public void addToList(ArrayList<RuntimeValue> addedElements){
        elements.addAll(addedElements);
    }

    public void addElement(RuntimeValue element){
        elements.add(element);
    }

    //hjelpemetode for å hente alle elementer
    public ArrayList<RuntimeValue> getElements(AspSyntax where) {
        return elements;
    }

    //hjelpemetode for å hente ut et element
    public RuntimeValue getElement(int index){
        return elements.get(index);
    }

    @Override
    public String getStringValue(String what, AspSyntax where){
        return this.toString();
    }

    @Override
    public String toString(){
        String printVal = "[";
        for(int i=0; i<elements.size(); i++){
            printVal += elements.get(i);
            if(i != elements.size()-1){
                printVal += ", ";
            }
        }
        printVal += "]";
        return printVal;
    }

    @Override
    public String typeName(){
        return "List";
    }

    @Override
    public boolean getBoolValue(String what, AspSyntax where){
        return(elements.size() != 0);
    }

    @Override
    public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where){
        if(v instanceof RuntimeIntegerValue){
            long value = v.getIntValue("* Operator", where);
            RuntimeListValue newList = new RuntimeListValue(new ArrayList<RuntimeValue>());

            for(int i = 0; i < value; i++){
                newList.addToList(elements);
            }
            return newList;
        }
        runtimeError("'*' undefined for " + typeName() + " and " + v.typeName() + "!", where);
        return null;
    }

    @Override
    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where){
        if(v instanceof RuntimeListValue){
            if(this.getElements(where).size() == v.getElements(where).size()){
                for(int i=0; i<this.getElements(where).size() -1; i++){
                    if(getElements(where).get(i) != v.getElements(where).get(i)){
                        return new RuntimeBoolValue(false);
                    }
                }
                return new RuntimeBoolValue(true);
            }else{
                return new RuntimeBoolValue(false);
            }
        }
        return new RuntimeBoolValue(false);
    }

    @Override
    public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where){
        if(v instanceof RuntimeListValue){
            if(this.getElements(where).size() == v.getElements(where).size()){
                for(int i=0; i<this.getElements(where).size() -1; i++){
                    if(getElements(where).get(i) != v.getElements(where).get(i)){
                        return new RuntimeBoolValue(true);
                    }
                }
                return new RuntimeBoolValue(false);
            }else{
                return new RuntimeBoolValue(true);
            }
        }
        return new RuntimeBoolValue(true);
    }

    @Override
    public RuntimeValue evalLen(AspSyntax where){
        return new RuntimeIntegerValue((long) elements.size());
    }

    @Override
    public RuntimeValue evalNot(AspSyntax where){
        return new RuntimeBoolValue(elements.size() == 0);
    }

    @Override
	  public RuntimeValue evalSubscription(RuntimeValue v, AspSyntax where) {
        int i = 0;

        if (v instanceof RuntimeIntegerValue){
            i = (int) v.getIntValue("Subscription", where);
        }else if(v instanceof RuntimeFloatValue){
            i = (int) v.getFloatValue("Subscription", where);
        }

        if(i > elements.size()-1){
            runtimeError("TypeError: Index out of bounds for " + typeName() + "!", where);
        }
        return elements.get(i);
    }

    @Override
    public void evalAssignElem(RuntimeValue inx, RuntimeValue val, AspSyntax where) {
      if (inx.getIntValue("Assignment", where) > elements.size()-1){
        runtimeError("TypeError: Index out of bounds for " + typeName() + "!", where);
      }
	     elements.set((int) inx.getIntValue("Assignment", where), val);
    }
}
