package helpers;

import Syntax.WrongSyntaxException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class ForLoopNode extends Node {
    HashMap<String,Object> forElement;
    ArrayList<HashMap<String, Object>> body;

    public ForLoopNode(String name, LinkedHashMap<String, Symbol> scope, HashMap<String,Object> element) throws Exception {
        super(name);
        symbolsDeclarations = scope;
        forElement = element;
        body = (ArrayList<HashMap<String, Object>> ) forElement.get("body");
        parseForBody();
    }

    @Override
    public String getMethod() {
        return null;
    }

    public void parseForBody() throws Exception {
        System.out.println("AAAA");
        //Evaluate expressions -> both should be int
        //Add identifier to scope
        //Parse body
    }

    public LinkedHashMap<String, Symbol> getSymbols() {
        return null;
    }
}
