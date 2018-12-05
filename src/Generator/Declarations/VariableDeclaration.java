package Generator.Declarations;

import Generator.Entities.Expression;
import Generator.Entities.Identifier;
import Generator.Types.Type;

public class VariableDeclaration extends SimpleDeclaration {
    private Identifier identifier;
    private Type type;
    private Expression expression;

    public VariableDeclaration(Identifier identifier, Type type, Expression expression) {
        this.identifier = identifier;
        this.type = type;
        this.expression = expression;
    }

    public String toJavaCode() {
        return type.toJavaCode() + " " + identifier.toJavaCode() + " = " + expression.toJavaCode() + ";";
    }
}
