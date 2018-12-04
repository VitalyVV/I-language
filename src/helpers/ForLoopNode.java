package helpers;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class ForLoopNode extends Node {

    private HashMap<String, Object> scope;

    public ForLoopNode(String name, HashMap<String, Object> scope){
        super(name);
        this.scope = scope;
    }
    @Override
    public String getMethod() {
        return null;
    }

    @Override
    public LinkedHashMap<String, Symbol> getSymbols() throws Exception {
        return null;
    }
}
