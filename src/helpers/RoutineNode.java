package helpers;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class RoutineNode extends Node {

    private HashMap<String, Object> routine;

    public RoutineNode(String name, HashMap<String, Object> routine){
        super(name);
        this.routine = routine;
    }

    public String getMethod(){
        return "Routine";
    }
    LinkedHashMap<String, Symbol> symbols = new LinkedHashMap<>();

    public LinkedHashMap<String, Symbol> getSymbols(){
        return null;
    }

    public HashMap<String, Object> getRoutine(){
        return routine;
    }
}
