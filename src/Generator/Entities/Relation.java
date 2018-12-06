package Generator.Entities;

import java.util.ArrayList;
import java.util.HashMap;

public class Relation {
    private Simple simLeft;
    private String op = "";
    private String rightType = "";
    private Object simRight;

    public Relation(HashMap<String, Object> map) {
        this.simLeft = new Simple((HashMap<String, Object>) map.get("left"));
        if (map.get("hasright").equals("true")){
            HashMap<String, Object> right = (HashMap<String, Object>)map.get("right");
            if (((String)right.get("is")).equals("simple")){
                rightType = "Simple";
                this.simRight = new Simple((HashMap<String, Object>)map.get("right"));
            }
            else if (((String)right.get("is")).equals("relation")){
                rightType = "Relation";
                this.simRight = new Relation((HashMap<String, Object>)map.get("right"));
            }
            this.op = (String)map.get("op");
        }
    }

    public String toJavaCode() {

        StringBuilder result = new StringBuilder(simLeft.toJavaCode());

        if (!(op.equals(""))){
            if (op.equals("=")) op = "==";
            if (op.equals("/=")) op = "!=";}

        if (rightType.equals("Simple"))
            result.append(op).append(((Simple)simRight).toJavaCode());
        else if (rightType.equals("Relation"))
            result.append(op).append(((Relation)simRight).toJavaCode());
        return result.toString();
    }
}
