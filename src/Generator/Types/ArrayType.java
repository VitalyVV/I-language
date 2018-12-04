package Generator.Types;

import Generator.Entities.Expression;

public class ArrayType extends UserType{
    private Expression expression;
    private Type type;

    public ArrayType(Expression expression, Type type) {
        this.expression = expression;
        this.type = type;
    }

    public Expression getExpression() {
        return expression;
    }

    public Type getType() {
        return type;
    }
}
