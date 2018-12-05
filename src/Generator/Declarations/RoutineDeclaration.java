package Generator.Declarations;

import Generator.Entities.Identifier;
import Generator.Types.Type;

import java.util.ArrayList;


public class RoutineDeclaration implements Declaration {
    private Identifier identifier;
    private Identifier returnIdentifier;
    private ArrayList<ParameterDeclaration> parameterDeclarations;
    private Type returnType;
    private Body body;


    public RoutineDeclaration(Identifier identifier, Identifier returnIdentifier,
                              ArrayList<ParameterDeclaration> parameterDeclarations, Type returnType, Body body) {
        this.identifier = identifier;
        this.returnIdentifier = returnIdentifier;
        this.parameterDeclarations = parameterDeclarations;
        this.returnType = returnType;
        this.body = body;
    }

    public String toJavaCode() {
        if (returnType != null) {
            StringBuilder result = new StringBuilder("public static " + returnType.getId() + " " + identifier.toJavaCode() + "(");
            for (int i = 0; i < parameterDeclarations.size(); i++) {
                if (i == 0) result.append(parameterDeclarations.get(i).toJavaCode());
                else if (i == parameterDeclarations.size() - 1)
                    result.append(parameterDeclarations.get(i).toJavaCode()).append("){");
            }
            result.append(body.toJavaCode()).append("return ").append(returnIdentifier.toJavaCode()).append(";}");
            return result.toString();
        }
        StringBuilder result = new StringBuilder("public static void " + identifier.toJavaCode() + "(");
        for (int i = 0; i < parameterDeclarations.size(); i++) {
            if (i == 0) result.append(parameterDeclarations.get(i).toJavaCode());
            else if (i == parameterDeclarations.size() - 1)
                result.append(parameterDeclarations.get(i).toJavaCode()).append("){");
        }
        result.append(body.toJavaCode()).append("}");
        return result.toString();
    }
}
