package helpers;

import Syntax.WrongSyntaxException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class IfNode extends Node {

    private LinkedHashMap<String, Symbol> originalScope;
    HashMap<String,Object> ifElement;
    ArrayList<HashMap<String, Object>> body;
    ArrayList<HashMap<String, Object>>  else_body;
    boolean hasElse = false;

    public IfNode(String name, LinkedHashMap<String, Symbol> scope, HashMap<String,Object> element) throws Exception {
        super(name);
        symbolsDeclarations = scope;
        originalScope = scope;
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

    public void parseBodies() throws Exception {
        HashMap<String,Object> expression = (HashMap<String, Object>) ifElement.get("expression") ;
        String expressionType = "boolean"; //calculateExpressionResult(ifElement.get("expression"));
        if (expressionType.equals("boolean") || (expressionType.equals("integer") && isIntBooleanable(expression)))
        {
            parseBody(body);
            if (hasElse)
            {
                symbolsDeclarations = originalScope;
                parseBody(else_body);
            }
        } else throw new WrongSyntaxException("If statement expression result is not boolean compatible");
    }

    public LinkedHashMap<String, Symbol> getSymbols() {
        return null;
    }
}
