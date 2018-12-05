package Generator.Entities;

import java.util.ArrayList;

public class Simple {
    private Factor main;
    private ArrayList<String> ops;
    private ArrayList<Factor> factors;

    public Simple(Factor main, ArrayList<String> ops, ArrayList<Factor> factors) {
        this.main = main;
        this.ops = ops;
        this.factors = factors;
    }

    public String toJavaCode(){
        StringBuilder result = new StringBuilder(main.toJavaCode());
        for (String op: ops
             ) {
            result.append(op).append(factors.remove(0).toJavaCode());
        }
        return result.toString();
    }
}
