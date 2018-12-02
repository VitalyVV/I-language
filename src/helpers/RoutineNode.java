package helpers;

import java.util.List;

public class RoutineNode extends Node {

    public RoutineNode(String name){
        super(name);
    }

    public String getMethod(){
        return "Routine";
    }

    public Node getChild(){
        //TODO add logic to fetch child from json
        return null;
    }

    public List<Symbol> getSymbols(){
        return null;
    }
}
