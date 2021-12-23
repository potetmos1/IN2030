package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;
import no.uio.ifi.asp.parser.*;
import java.util.ArrayList;

public class RuntimeFunc extends RuntimeValue{
    public ArrayList<RuntimeValue> params = new ArrayList<RuntimeValue>();

    AspFuncDef funcDef;
    RuntimeScope scope;
    String name;

    public RuntimeFunc(RuntimeValue rv, ArrayList<RuntimeValue> params, RuntimeScope curScope, AspFuncDef def){
        name = rv.showInfo();
        this.params = params;
        scope = curScope;
        this.funcDef = def;
    }

    public RuntimeFunc(String name){
        this.name = name;
    }

    @Override
    public String typeName(){
        return "function";
    }

    @Override
    public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where){
        RuntimeValue returnVal = null;
        /* debug print
        System.out.println("Running function with parameters: ");
        for (RuntimeValue x: actualParams)
        {
          System.out.println(x);
        }

        */
        RuntimeValue val;

        if(actualParams.size() == params.size()){
            RuntimeScope newScope = new RuntimeScope(this.scope);
            //System.out.println(params.size());
            for(int i = 0; i < params.size(); i++){
                //RuntimeValue val = newScope.find(actualParams.get(i).getStringValue("string", where), funcDef);
                val = actualParams.get(i);
                if(val != null){
                    newScope.assign(params.get(i).getStringValue("string", where), val);
                }else{
                    newScope.assign(params.get(i).getStringValue("string", where), actualParams.get(i));
                }
            }
            try{
                val = funcDef.runFunc(newScope);
            }catch(RuntimeReturnValue v){
                return v.value;
            }

        }else{
            Main.error("not matching parameters");
        }
        return returnVal;
    }
}
