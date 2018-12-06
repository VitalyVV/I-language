package Generator.Entities;

import java.util.ArrayList;
import java.util.HashMap;

public class Expression {
    private Relation relLeft;
    private String op;
    private Relation relRight;

    public Expression(HashMap<String, Object> map) {
        this.relLeft = new Relation((HashMap<String, Object>)map.get("left"));
        if (map.get("hasright").equals("true")){
            this.relRight = new Relation((HashMap<String, Object>)map.get("right"));
            this.op = (String)map.get("op");
        }
    }

    public String toJavaCode() {
        StringBuilder result = new StringBuilder(relLeft.toJavaCode());
            switch (op) {
                case "and": {
                    result.append(" && ").append(relRight.toJavaCode());
                }
                case "or": {
                    result.append(" || ").append(relRight.toJavaCode());
                }
                case "xor": {
                    result.append(" ^ ").append(relRight.toJavaCode());
                }
                default:
                    break;
            }
        return result.toString();
    }
}
