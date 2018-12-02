package helpers;

import java.util.*;

public class ProgramNode extends Node {

    public ProgramNode(String name){
        super(name);
    }

    private ArrayList<HashMap<String, Object>> children;

    private LinkedHashMap<String, Symbol> symbolsDeclarations = new LinkedHashMap<>();

    public String getMethod(){
        return "Root";
    }

    public ProgramNode(String name, ArrayList<HashMap<String, Object>> children){
        super(name);
        this.children = children;
    }

    public Node getChild(){
        //TODO add logic to fetch child from json
        //return new RoutineNode("foo");
        return null;
    }

    public LinkedHashMap<String, Symbol> getSymbols() throws Exception{
        for (HashMap<String, Object> elem: children){

            HashMap<String, Object> rootUnit = ((HashMap<String, Object>) elem.get("Content"));
            String unit = (String) rootUnit.get("statement");
            if (unit.equals("var")){
                if (((String)rootUnit.get("hastype")).equals("true")){
                    Symbol s = new Symbol(getType(rootUnit.get("type")), (String) rootUnit.get("name"), rootUnit);
                    symbolsDeclarations.put((String) rootUnit.get("name"), s);
                }
                else {
                    String valueType = calculateExpressionResult(rootUnit.get("expression"));
                    Symbol s = new Symbol(valueType, (String) rootUnit.get("name"), rootUnit);
                    symbolsDeclarations.put((String) rootUnit.get("name"), s);

                }
            }
            if(unit.equals("declaration")){

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

    private String calculateExpressionResult(Object o){
        return null;
    }
}
