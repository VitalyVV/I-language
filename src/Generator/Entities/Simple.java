package Generator.Entities;

import java.util.ArrayList;
import java.util.HashMap;

public class Simple {
    private Factor leftFactor;
    private String op;
    private Object rigthFactor;
    private String rightType;

    public Simple(HashMap<String, Object> map) {
        HashMap<String, Object> mm = new HashMap<>();
        this.leftFactor = new Factor((HashMap<String, Object>)map.get("left"));
        if (map.get("hasright").equals("true")){
            HashMap<String, Object> right = (HashMap<String, Object>)map.get("right");
            if (((String)right.get("is")).equals("factor")){
                rightType = "Factor";
                this.rigthFactor = new Factor((HashMap<String, Object>)map.get("right"));
            }
            else if (((String)right.get("is")).equals("relation")){
                rightType = "Simple";
                this.rigthFactor = new Simple((HashMap<String, Object>)map.get("right"));
            }
            this.op = (String)map.get("op");
        }
    }

    public String toJavaCode() {
        StringBuilder result = new StringBuilder(main.toJavaCode());
        for (String op : ops
                ) {
            result.append(op).append(factors.remove(0).toJavaCode());
        }
        return result.toString();
    }
}