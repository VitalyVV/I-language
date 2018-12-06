package helpers;

import Syntax.WrongSyntaxException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class WhileNode extends Node {

    HashMap<String,Object> whileElement;
    ArrayList<HashMap<String, Object>> body;

    public WhileNode(String name, LinkedHashMap<String, Symbol> scope, HashMap<String, Object> types, HashMap<String,Object> element) throws Exception {
        super(name);
        symbolsDeclarations = (LinkedHashMap<String, Symbol>)scope.clone();
        typeMappings = (HashMap<String, Object>) types.clone();
        whileElement = element;
        body = (ArrayList<HashMap<String, Object>> ) whileElement.get("body");
        parseWhileBody();
    }

    @Override
    public String getMethod() {
        return null;
    }

    public void parseWhileBody() throws Exception {
        HashMap<String,Object> expression = (HashMap<String, Object>) whileElement.get("expression") ;
        String expressionType = "boolean"; //calculateExpressionResult(ifElement.get("expression"));
        if (expressionType.equals("boolean") || (expressionType.equals("integer") && isIntBooleanable(expression)))
        {
            parseBody(body);

        } else throw new WrongSyntaxException("If statement expression result is not boolean compatible");
    }

    public LinkedHashMap<String, Symbol> getSymbols() {
        return null;
    }
}
