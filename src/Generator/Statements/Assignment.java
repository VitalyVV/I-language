package Generator.Statements;

import Generator.Entities.Expression;
import Generator.Entities.ModifiablePrimary;

public class Assignment extends Statement {
    private ModifiablePrimary modifiablePrimary;
    private Expression expression;

    public Assignment(ModifiablePrimary modifiablePrimary, Expression expression) {
        this.modifiablePrimary = modifiablePrimary;
        this.expression = expression;
    }

    public String toJavaCode() {
        return modifiablePrimary.toJavaCode() + "=" + expression.toJavaCode() + ";";
    }
}
