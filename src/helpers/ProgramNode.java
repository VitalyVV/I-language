package helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ProgramNode extends Node {

    public ProgramNode(String name){
        super(name);
    }

    private ArrayList<HashMap<String, Object>> children;

    private List<Symbol> symbolsDeclarations = new LinkedList<>();

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

    public List<Symbol> getSymbols(){
        for (HashMap<String, Object> elem: children){

            HashMap<String, Object> rootUnit = ((HashMap<String, Object>) elem.get("Content"));
            String unit = (String) rootUnit.get("statement");
            if (unit.equals("var")){
                if (((String)rootUnit.get("hastype")).equals("true")){
                    Symbol s = new Symbol(getType(rootUnit.get("type")), (String) rootUnit.get("name"), rootUnit);
                    symbolsDeclarations.add(s);
                }
                else {

                }
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
        return null;
    }

    private String getType(Object unit){
        return null;
    }
}
