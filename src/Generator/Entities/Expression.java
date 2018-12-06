package Generator.Entities;

import java.util.ArrayList;
import java.util.HashMap;

public class Expression {
    private Relation relLeft;
    private String op = "";
    //private Relation relRight;
    private Object right;
    private String rightType = "";

    public Expression(HashMap<String, Object> map) {
        this.relLeft = new Relation((HashMap<String, Object>)map.get("left"));
        if (map.get("hasright").equals("true")){
            HashMap<String, Object> right = (HashMap<String, Object>)map.get("right");
            if (((String)right.get("is")).equals("relation")){
                rightType = "Relation";
                this.right = new Relation((HashMap<String, Object>)map.get("right"));
            }
            else if (((String)right.get("is")).equals("expression")){
                rightType = "Expression";
                this.right = new Expression((HashMap<String, Object>)map.get("right"));
            }
            this.op = (String)map.get("op");
        }
    }

    public String toJavaCode() {

        StringBuilder result = new StringBuilder(relLeft.toJavaCode());

                if (op.equals("and")) {
                    if (rightType.equals("Relation"))
                    result.append(" && ").append(((Relation)right).toJavaCode());
                    else if (rightType.equals("Expression")){
                        result.append(" && ");
                    String res = ((Expression)right).toJavaCode();
                    result.append(res);}
                }
                else if (op.equals("or")) {

                    if (rightType.equals("Relation"))
                        result.append(" || ").append(((Relation)right).toJavaCode());
                    else if (rightType.equals("Expression"))
                        result.append(" || ").append(((Expression)right).toJavaCode());

                }
                else if (op.equals("xor")) {
                    if (rightType.equals("Relation"))
                        result.append(" ^ ").append(((Relation)right).toJavaCode());
                    else if (rightType.equals("Expression"))
                        result.append(" ^ ").append(((Expression)right).toJavaCode());
                }
        return result.toString();
    }
}
