package helpers;

import Syntax.WrongSyntaxException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class IfNode extends Node {

    private LinkedHashMap<String, Symbol> originalScope;
    HashMap<String,Object> ifElement;
    ArrayList<HashMap<String, Object>> body;
    ArrayList<HashMap<String, Object>>  else_body;
    boolean hasElse = false;

    public IfNode(String name, LinkedHashMap<String, Symbol> scope, HashMap<String, Object> types, HashMap<String, Object> element, LinkedList<RoutineNode> routines, LinkedList<String> namesRoutines) throws Exception {
        super(name);
        setRoutines( routines, namesRoutines);
        symbolsDeclarations = (LinkedHashMap<String, Symbol>) scope.clone();
        typeMappings = (HashMap<String, Object>) types.clone();
        originalScope = (LinkedHashMap<String, Symbol>) scope.clone();
        ifElement = element;
        body = (ArrayList<HashMap<String, Object>> ) ifElement.get("body");
        if (ifElement.containsKey("else_body"))
        {
            hasElse = true;
            else_body = (ArrayList<HashMap<String, Object>> ) ifElement.get("else_body");
        }
        parseBodies();
    }

    @Override
    public String getMethod() {
        return null;
    }

    protected boolean isTrue(HashMap<String,Object> value)
    {
        HashMap<String,Object> val = value;
        while(true)
        {
            if (val.containsKey("type") && (val.get("type").equals("integer") || val.get("type").equals("boolean"))) break;
            if (val.containsKey("hasright") && val.get("hasright").equals("true")) return false;
            if (val.containsKey("type") && val.get("type").equals("modifiable")) return false;
            val = (HashMap<String, Object>) val.get("left");
        }
        if (val.get("value").equals("true") || val.get("value").equals("1"))  return true; else return false;
    }

    public void parseBodies() throws Exception {
        HashMap<String,Object> expression = (HashMap<String, Object>) ifElement.get("expression") ;
        String expressionType = calculateExpressionResult(ifElement.get("expression"));
        if (expressionType.equals("boolean") || (expressionType.equals("integer") && isIntBooleanable(expression))) {
            boolean statementTrue = isTrue((HashMap<String, Object>) ifElement.get("expression"));
            if (statementTrue)
            {
                parseBody(body);
            }
            else if (hasElse) {
                    symbolsDeclarations = originalScope;
                    parseBody(else_body);
                }

        } else throw new WrongSyntaxException("If statement expression result is not boolean compatible");
    }

    public LinkedHashMap<String, Symbol> getSymbols() {
        return null;
    }
}
