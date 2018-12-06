package Generator.Entities;

import java.util.ArrayList;
import java.util.HashMap;

public class Factor {
    private Summand left;
    private String op = "";
    private Object right;
    private String rightType = "";

    public Factor(HashMap<String, Object> map) {
        this.left = new Summand((HashMap<String, Object>) map.get("left"));
        if (map.get("hasright").equals("true")){
            HashMap<String, Object> right = (HashMap<String, Object>)map.get("right");
            if (((String)right.get("is")).equals("factor")){
                rightType = "Factor";
                this.right = new Factor((HashMap<String, Object>)map.get("right"));
            }
            else if (((String)right.get("is")).equals("summand")){
                rightType = "Summand";
                this.right = new Summand((HashMap<String, Object>)map.get("right"));
            }
            this.op = (String)map.get("op");
        }
    }

//    public Factor(HashMap<String, Object> map) {
//        this.main = new Summand((HashMap<String, Object>) map.get("left"));
//    }

    public String toJavaCode() {
        StringBuilder result = new StringBuilder(left.toJavaCode());
        if (rightType.equals("Factor"))
            result.append(op).append(((Factor) right).toJavaCode());
        else if (rightType.equals("Summand"))
            result.append(op).append(((Summand)right).toJavaCode());
        return result.toString();
    }
}
