package helpers;

import Syntax.WrongSyntaxException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class ForLoopNode extends Node {
    HashMap<String,Object> forElement;
    ArrayList<HashMap<String, Object>> body;

    public ForLoopNode(String name, LinkedHashMap<String, Symbol> scope, HashMap<String, Object> types, HashMap<String, Object> element, LinkedList<RoutineNode> routines, LinkedList<String> namesRoutines) throws Exception {
        super(name);
        setRoutines( routines, namesRoutines);
        symbolsDeclarations = scope;
        this.typeMappings = (HashMap<String, Object>) types.clone();
        forElement = element;
        body = (ArrayList<HashMap<String, Object>> ) forElement.get("body");
        parseForBody();
    }

    @Override
    public String getMethod() {
        return null;
    }

    public void parseForBody() throws Exception {
        HashMap<String,Object> ranges = (HashMap<String, Object>) forElement.get("range");
        String range1 = calculateExpressionResult(ranges.get("expression_from"));
        String range2 = calculateExpressionResult(ranges.get("expression_to"));
        if(range1.equals("integer") && range2.equals("integer"))
        {
            Symbol s = new Symbol("integer", (String) forElement.get("identifier"), null);

            symbolsDeclarations.put((String) forElement.get("identifier"), s);
            parseBody((ArrayList<HashMap<String, Object>>) forElement.get("body"));
        } else throw new WrongSyntaxException("Result of one of the range expression elements is not integer");
        //Evaluate expressions -> both should be int
        //Add identifier to scope
        //Parse body
    }

    public LinkedHashMap<String, Symbol> getSymbols() {
        return null;
    }
}