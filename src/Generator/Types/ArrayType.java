package Generator.Types;

import Generator.Entities.Expression;

public class ArrayType extends UserType {
    private Expression expression;
    private Type type;

    public ArrayType(Expression expression, Type type) {
        this.expression = expression;
        this.type = type;
    }

    @Override
    public String getId() {
        return type.getId();
    }

    public String toJavaCode() {
        if (type.getClass().getName().equals("RecordType")) return type.getId() + "[" + expression.toJavaCode() + "]";
        return type.toJavaCode() + "[" + expression.toJavaCode() + "]";
    }
}
