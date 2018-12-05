package Generator.Declarations;

import Generator.Entities.Identifier;
import Generator.Types.RecordType;
import Generator.Types.Type;

public class RecordTypeDeclaration extends SimpleDeclaration {
    private Identifier identifier;
    private Type type;

    public RecordTypeDeclaration(Identifier identifier, RecordType type) {
        this.identifier = identifier;
        this.type = type;
    }

    public String toJavaCode() {
        return "public static class " + identifier.toJavaCode() + " " + type.toJavaCode();
    }
}
