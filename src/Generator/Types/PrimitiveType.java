package Generator.Types;

import java.util.HashMap;

public class PrimitiveType implements Type {
    private String type;

    public PrimitiveType(HashMap<String,Object> primType) {
        String type = (String) primType.get("primitive");
        if (!(type.equals("integer") || type.equals("real") || type.equals("boolean"))) {
            throw new RuntimeException("Not a primitive type: " + type);
        }
        this.type = type;
    }

    public String toJavaCode() {
        if (type.equals("real")) type = "double";
        return type;
    }

    @Override
    public String getId() {
        return type;
    }

}
