package Generator.Declarations;

import Generator.Entities.Expression;
import Generator.Entities.Identifier;
import Generator.Types.PrimitiveType;
import Generator.Types.Type;

import java.util.HashMap;

public class VariableDeclaration extends SimpleDeclaration {
    private Identifier identifier;
    private Type type;
    private Expression expression;

    public VariableDeclaration(HashMap<String, Object> map, Expression expression) {
        this.identifier = new Identifier((String) map.get("name"));
        if (map.get("hastype").equals("true")) {
            if (((HashMap) map.get("type")).containsKey("primitive")) {
                type = new PrimitiveType((String) ((HashMap) map.get("type")).get("primitive"));
            } else if (((HashMap) map.get("type")).containsKey("record")) {

            }
            else if (((HashMap) map.get("type")).containsKey("array")){

            }

        }
        this.expression = expression;
    }

    public String toJavaCode() {
        return type.toJavaCode() + " " + identifier.toJavaCode() + " = " + expression.toJavaCode() + ";";
    }
}
