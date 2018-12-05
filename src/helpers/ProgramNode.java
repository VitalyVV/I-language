package helpers;
import Syntax.WrongSyntaxException;
import com.sun.corba.se.impl.io.TypeMismatchException;
import jdk.nashorn.internal.runtime.regexp.joni.exception.SyntaxException;

import java.util.*;

//I think that functions for processing

public class ProgramNode extends Node {

    public ProgramNode(String name){
        super(name);
    }

    public String getMethod(){
        return "Root";
    }

    public ProgramNode(String name, ArrayList<HashMap<String, Object>> children){
        super(name);
        this.children = children;

    }

    public LinkedHashMap<String, Symbol> getSymbols(){
        return symbolsDeclarations;
    }

    //Start method to go down the tree and get the types of objects
    public void createSymbols() throws Exception{
        for (HashMap<String, Object> elem: children){

            HashMap<String, Object> rootUnit = ((HashMap<String, Object>) elem.get("Content"));
            String unit = (String) rootUnit.get("statement");
            if (unit.equals("var")) {//variable declaration type extraction
                if (symbolsDeclarations.containsKey(rootUnit.get("name"))) throw new SyntaxException("Variable has already been declared: "+ rootUnit.get("name"));
                if (((String) rootUnit.get("hastype")).equals("true")) {
                    //if type is already declared via "var a: integer"
                    Symbol s = null;
                    String varType = getType(rootUnit.get("type"));
                    if (varType.equals("record") || varType.equals("array"))
                    {
                        s = new Symbol(varType, (String) rootUnit.get("name"), rootUnit);
                    } else if (rootUnit.containsKey("value")) { //Vars that have type may have "is" expressions too
                        //Inference expression type
                        String expressionResult = calculateExpressionResult(rootUnit.get("value"));
                        if (varType.equals("boolean") &&  expressionResult.equals("integer") && !isIntBooleanable(rootUnit)) throw new WrongSyntaxException("Can't assign non 1 or 0 int to boolean");
                        //If expression type is the same as declared type
                        if (expressionResult.equals(varType) || (varType.equals("boolean") &&  expressionResult.equals("integer")))
                        {
                            s = new Symbol(varType, (String) rootUnit.get("name"), rootUnit);
                        } else throw new TypeMismatchException("\nWrong expression result type for: " + rootUnit.get("name") +
                                ".\nExpected: "+ varType+". Got: " + expressionResult);

                    } else s = new Symbol(varType, (String) rootUnit.get("name"), rootUnit);

                    symbolsDeclarations.put((String) rootUnit.get("name"), s);
                } else {
                    if (symbolsDeclarations.containsKey(rootUnit.get("name"))) throw new SyntaxException("Variable has already been declared: "+ rootUnit.get("name"));
                    //if type is declared via expression calculation like "var a: integer is (5+5)*10"
                    String valueType = calculateExpressionResult(rootUnit.get("expression"));
                    Symbol s = new Symbol(valueType, (String) rootUnit.get("name"), rootUnit);
                    symbolsDeclarations.put((String) rootUnit.get("name"), s);
                }
            } else if(unit.equals("declaration")){//if we have routine declaration "routine a() : integer"
                if (namesRoutines.contains(rootUnit.get("name"))) throw new SyntaxException("Routine has already been declared: "+ rootUnit.get("name"));
                RoutineNode routine = new RoutineNode((String) rootUnit.get("name"), rootUnit);
                //we add it separate list of routines
                routines.add(routine);
                //we add name of routine to find previously declared ones + to check if we actually have declared it
                namesRoutines.add((String) rootUnit.get("name"));
                symbolsDeclarations.put((String) rootUnit.get("name"),
                        new Symbol("routine", (String) rootUnit.get("name"), routine));

                routineEndTypes.put((String) rootUnit.get("name"), getLastKnownType());
            }else if(unit.equals("type")){//if we have type declaration
                if (typeMappings.containsKey(rootUnit.get("name"))) throw new SyntaxException("Type has already been declared: "+ rootUnit.get("name"));
                //Add type mapping to the list
                typeMappings.put((String)rootUnit.get("name"),getType(rootUnit.get("type")));
            } else throw new Exception("Invalid syntax. Routine or declaration expected");
        }

        entries = symbolsDeclarations.entrySet().iterator();

    }

}
