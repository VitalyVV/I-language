package Generator.Declarations;

import Generator.Entities.Identifier;
import Generator.Types.Type;

import java.util.ArrayList;


public class RoutineDeclaration implements Declaration {
    private Identifier identifier;
    private ArrayList<ParameterDeclaration> parameterDeclarations;
    private Type returnType;
    private Body body;

    public RoutineDeclaration(Identifier identifier, ArrayList<ParameterDeclaration> parameterDeclarations, Type returnType, Body body) {
        this.identifier = identifier;
        this.parameterDeclarations = parameterDeclarations;
        this.returnType = returnType;
        this.body = body;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public ArrayList<ParameterDeclaration> getParameterDeclarations() {
        return parameterDeclarations;
    }

    public Body getBody() {
        return body;
    }

    public Type getReturnType() {
        return returnType;
    }
}
