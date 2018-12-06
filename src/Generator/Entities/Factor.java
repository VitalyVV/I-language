package Generator.Entities;

import java.util.ArrayList;
import java.util.HashMap;

public class Factor {
    private Summand left;
    private String op;
    private Object right;
    private String rightType;

    public Factor(HashMap<String, Object> map) {
        this.left = new Summand((HashMap<String, Object>) map.get("left"));
        if (map.get("hasright").equals("true")){
            HashMap<String, Object> right = (HashMap<String, Object>)map.get("right");
            if (((String)right.get("is")).equals("factor")){
                rightType = "Factor";
                this.right = new Summand((HashMap<String, Object>)map.get("right"));
            }
            else if (((String)right.get("is")).equals("relation")){
                rightType = "Simple";
                this.right = new Factor((HashMap<String, Object>)map.get("right"));
            }
            this.op = (String)map.get("op");
        }
    }

//    public Factor(HashMap<String, Object> map) {
//        this.main = new Summand((HashMap<String, Object>) map.get("left"));
//    }



    public String toJavaCode() {
        StringBuilder result = new StringBuilder(left.toJavaCode());
        for (String op: ops
             ) {
            result.append(op).append(summands.remove(0).toJavaCode());
        }
        return result.toString();
    }
}
