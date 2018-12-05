package Generator.Declarations;

import Generator.Entities.Identifier;
import Generator.Types.Type;

public class TypeDeclaration extends SimpleDeclaration {
    private Identifier identifier;
    private Type type;

    public TypeDeclaration(Identifier identifier, Type type) {
        this.identifier = identifier;
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public Identifier getIdentifier() {
        return identifier;
    }
}
