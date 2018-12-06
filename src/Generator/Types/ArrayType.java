package Generator.Types;

import Generator.Entities.Expression;

import java.util.HashMap;

public class ArrayType extends UserType {
    private Expression expression;
    private Type type;

    public ArrayType(HashMap<String, Object> map) {
        this.expression = new Expression((HashMap<String, Object>) map.get("length"));
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
