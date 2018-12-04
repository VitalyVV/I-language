package helpers;

import com.sun.corba.se.impl.io.TypeMismatchException;

import javax.naming.OperationNotSupportedException;
import java.util.*;

//I think that functions for processing

public class ProgramNode extends Node {

    public ProgramNode(String name){
        super(name);
    }

    private ArrayList<HashMap<String, Object>> children;

//    //list of variable symbols only
//    private LinkedHashMap<String, Symbol> symbolsDeclarations = new LinkedHashMap<>();


    //list of routines only
    private LinkedList<RoutineNode> routines = new LinkedList<>();
    private LinkedList<String> namesRoutines = new LinkedList<>();
//    Iterator<Map.Entry<String,Symbol>>entries = symbolsDeclarations.entrySet().iterator();

//    //type declarations
//    HashMap<String,String> typeMappings = new HashMap<>();

//    private int currentChild = 0;


    public String getMethod(){
        return "Root";
    }

    public ProgramNode(String name, ArrayList<HashMap<String, Object>> children){
        super(name);
        this.children = children;

    }

//    public Map.Entry<String, Symbol> getChild(){
//
//        if (currentChild>symbolsDeclarations.size()-1) return null;
//        Map.Entry<String,Symbol> entry = entries.next();
//        //update the pointer for routine we have not visited before
//        return entry;
//    }



    //Start method to go down the tree and get the types of objects
    public LinkedHashMap<String, Symbol> getSymbols() throws Exception{
        for (HashMap<String, Object> elem: children){

            HashMap<String, Object> rootUnit = ((HashMap<String, Object>) elem.get("Content"));
            String unit = (String) rootUnit.get("statement");
            if (unit.equals("var")) {//variable declaration type extraction
                if (((String) rootUnit.get("hastype")).equals("true")) {
                    //if type is already declared via "var a: integer"
                    Symbol s = null;
                    //Vars that have type may have "is" expressions too
                    if (rootUnit.containsKey("value"))
                    {
                        String varType = getType((HashMap<String,Object>) rootUnit.get("type"));

                        //Inference expression type
                        String expressionResult = calculateExpressionResult(rootUnit.get("value"));
                        //If expression type is the same as declared type
                        if (expressionResult.equals(varType))
                        {
                            s = new Symbol(getType(rootUnit.get("type")), (String) rootUnit.get("name"), rootUnit);
                        } else throw new TypeMismatchException("\nWrong expression result type for: " + rootUnit.get("name") +
                                ".\nExpected: "+ varType+". Got: " + expressionResult);

                    } else s = new Symbol(getType(rootUnit.get("type")), (String) rootUnit.get("name"), rootUnit);

                    symbolsDeclarations.put((String) rootUnit.get("name"), s);
                } else {
                    //if type is declared via expression calculation like "var a: integer is (5+5)*10"
                    String valueType = calculateExpressionResult(rootUnit.get("expression"));
                    Symbol s = new Symbol(valueType, (String) rootUnit.get("name"), rootUnit);
                    symbolsDeclarations.put((String) rootUnit.get("name"), s);
                }
            } else if(unit.equals("declaration")){//if we have routine declaration "routine a() : integer"
                RoutineNode routine = new RoutineNode((String) rootUnit.get("name"), rootUnit);
                //we add it separate list of routines
                routines.add(routine);
                //we add name of routine to find previously declared ones + to check if we actually have declared it
                namesRoutines.add((String) rootUnit.get("name"));
                symbolsDeclarations.put((String) rootUnit.get("name"), new Symbol("routine", (String) rootUnit.get("name"), routine));
            }else if(unit.equals("type")){//if we have type declaration
                //Add type mapping to the list
                typeMappings.put((String)rootUnit.get("name"),getType(rootUnit.get("type")));
            } else throw new Exception("Invalid syntax. Routine or declaration expected");
        }



//        int a = 5;
//        int b = 6;
//        Symbol s1 = new Symbol(a, "a");
//        Symbol s2 = new Symbol(b, "b");
//
//        List<Symbol> s = new LinkedList<Symbol>();
//        s.add(s1);
//        s.add(s2);
//        return s;

        entries = symbolsDeclarations.entrySet().iterator();
        return symbolsDeclarations;
    }


    //get type for variable using the one declared in specified hashmap (also perform a check for a variable to
    // have been  already declared)
//    private String getType(Object unit) throws Exception{
//        HashMap<String, Object> a= (HashMap<String, Object>) unit;
//        if (a.containsKey("primitive")){
//            return (String)a.get("primitive");
//        }
//        else if (a.containsKey("identifier")){
//            String typeVarName = (String)a.get("identifier");
//            if (symbolsDeclarations.keySet().contains(typeVarName)){
//                return symbolsDeclarations.get(typeVarName).getType();
//            } else if (typeMappings.containsKey(typeVarName))
//            {
//                //Get mapped basic type from the list
//                return typeMappings.get(typeVarName);
//            }
//            else throw new Exception("No such identifier declared: "+typeVarName);
//        }
//        else if (a.containsKey("array")){
//            return "array";
//        }
//        else if (a.containsKey("record")){
//            return "record";
//        }
//        else {
//            Map.Entry<String,Object> entry = a.entrySet().iterator().next();
//            throw  new Exception("No such type exists: "+entry.getKey());
//        }
//    }



}
