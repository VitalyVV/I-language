package Generator.Declarations;

import Generator.Entities.Identifier;
import Generator.Types.Type;

public class ParameterDeclaration {
    private Identifier identifier;
    private Type type;

    public ParameterDeclaration(Identifier identifier, Type type) {
        this.identifier = identifier;
        this.type = type;
    }

    public String toJavaCode() {
        return type.getId() + " " + identifier.toJavaCode();
    }
}
