package helpers;

import java.util.*;

//I think that functions for processing

public class ProgramNode extends Node {

    public ProgramNode(String name){
        super(name);
    }

    private ArrayList<HashMap<String, Object>> children;

    //list of variable symbols only
    private LinkedHashMap<String, Symbol> symbolsDeclarations = new LinkedHashMap<>();


    //list of routines only
    private LinkedList<RoutineNode> routines = new LinkedList<>();
    private LinkedList<String> namesRoutines = new LinkedList<>();
    private int currentChild = 0;


    public String getMethod(){
        return "Root";
    }

    public ProgramNode(String name, ArrayList<HashMap<String, Object>> children){
        super(name);
        this.children = children;

    }

    public RoutineNode getChild(){

        if (currentChild>routines.size()-1) return null;
        RoutineNode rnode =  routines.get(currentChild);
        currentChild++; //update the pointer for routine we have not visited before
        return rnode;
    }

    //Start method to go down the tree and get the types of objects
    public LinkedHashMap<String, Symbol> getSymbols() throws Exception{
        for (HashMap<String, Object> elem: children){

            HashMap<String, Object> rootUnit = ((HashMap<String, Object>) elem.get("Content"));
            String unit = (String) rootUnit.get("statement");
            if (unit.equals("var")){//variable declaration type extraction
                if (((String)rootUnit.get("hastype")).equals("true")){

                    //if type is already declared via "var a: integer"
                    Symbol s = new Symbol(getType(rootUnit.get("type")), (String) rootUnit.get("name"), rootUnit);
                    symbolsDeclarations.put((String) rootUnit.get("name"), s);
                }
                else {
                    //if type is declared via expression calculation like "var a: integer is (5+5)*10"
                    String valueType = calculateExpressionResult(rootUnit.get("expression"));
                    Symbol s = new Symbol(valueType, (String) rootUnit.get("name"), rootUnit);
                    symbolsDeclarations.put((String) rootUnit.get("name"), s);

                }
            }

            //if we have routine declaration "routine a() : integer"
            else if(unit.equals("declaration")){
                RoutineNode routine = new RoutineNode((String) rootUnit.get("name"), rootUnit);

                //we add it separate list of routines
                routines.add(routine);
                //we add name of routine to find previously declared ones + to check if we actually have declared it
                namesRoutines.add((String) rootUnit.get("name"));
            }
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
        return symbolsDeclarations;
    }


    //get type for variable using the one declared in specified hashmap (also perform a check for a variable to
    // have been  already declared)
    private String getType(Object unit) throws Exception{
        HashMap<String, Object> a= (HashMap<String, Object>) unit;
        if (a.containsKey("primitive")){
            return (String)a.get("primitive");
        }
        else if (a.containsKey("identifier")){
            String typeVarName = (String)a.get("identifier");
            if (symbolsDeclarations.keySet().contains(typeVarName)){
                return symbolsDeclarations.get(typeVarName).getType();
            }
            else throw new Exception("No such identifier declared: "+typeVarName);
        }
        else if (a.containsKey("array")){
            return "array";
        }
        else if (a.containsKey("record")){
            return "record";
        }
        else {
            Map.Entry<String,Object> entry = a.entrySet().iterator().next();
            throw  new Exception("No such type exists: "+entry.getKey());
        }
    }

    //just go down the tree and get the type of expression - TODO
    private String calculateExpressionResult(Object o) throws Exception{

        HashMap<String, Object> hashmaped = (HashMap<String, Object>) o;

        if (((String)hashmaped.get("is")).equals("primary")){
            String typePrimary = ((String)hashmaped.get("type"));
            if (typePrimary.equals("boolean") || typePrimary.equals("integer")||typePrimary.equals("real")){
                return typePrimary;
            }
            else if (typePrimary.equals("modifiable")){
                return null;
            }
            else if (typePrimary.equals("routinecall")){
                HashMap<String, Object> hashmapedRotineCall = (HashMap<String, Object>) hashmaped.get("type");
                String name = (String)hashmapedRotineCall.get("name");
                if (namesRoutines.contains(name)){
                    int index = namesRoutines.indexOf(name);
                    RoutineNode rnode = routines.get(index);
                    return rnode.getResultType();
                }
            }
            else throw new Exception("No such type exists: "+typePrimary);
        }
        String result = calculateExpressionResult(hashmaped.get("left"));
        if (((String)hashmaped.get("is")).equals("factor")){
            if (((String)hashmaped.get("hasright")).equals("true")){
                String resultRight = calculateExpressionResult(hashmaped.get("right"));
                if (checkOperable(result, resultRight)){
                    return getTypesResult(result, resultRight);
                }
            }
        }

        return null;
    }


}
