package Generator.Types;

import Generator.Declarations.VariableDeclaration;
import Generator.Entities.Identifier;

import java.util.ArrayList;

public class RecordType extends UserType {
    private Identifier identifier;
    private ArrayList<VariableDeclaration> declarations;

    public RecordType(Identifier identifier, ArrayList<VariableDeclaration> declarations) {
        this.identifier = identifier;
        this.declarations = declarations;
    }

    public String getId() {
        return identifier.toJavaCode();
    }

    public String toJavaCode() {
        StringBuilder result = new StringBuilder("{");
        for (VariableDeclaration declaration : declarations
                ) {
            result.append("public static ").append(declaration.toJavaCode()).append("\n");
        }
        return result.append("}").toString();
    }

}
