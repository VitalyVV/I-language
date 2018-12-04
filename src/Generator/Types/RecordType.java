package Generator.Types;

import Generator.Declarations.VariableDeclaration;

public class RecordType extends UserType {
    private VariableDeclaration declaration;

    public RecordType(VariableDeclaration declaration) {
        this.declaration = declaration;
    }

    public VariableDeclaration getDeclaration() {
        return declaration;
    }
}
