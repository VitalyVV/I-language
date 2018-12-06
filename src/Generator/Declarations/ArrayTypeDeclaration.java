package Generator.Declarations;

import Generator.Entities.Identifier;
import Generator.Types.ArrayType;
import Generator.Types.Type;

public class ArrayTypeDeclaration extends SimpleDeclaration {
    private Type type;
    private Identifier identifier;

    public ArrayTypeDeclaration(Identifier identifier, ArrayType type) {
        this.type = type;
        this.identifier = identifier;
    }

    public String toJavaCode() {
        return "public static " + type.getId() + "[]" + identifier.toJavaCode() + " = new " + type.toJavaCode();
    }
}
