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
}
