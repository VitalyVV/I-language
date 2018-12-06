package Generator.Entities;

import java.util.ArrayList;
import java.util.HashMap;

public class Factor {
    private Summand main;
    private ArrayList<String> ops;
    private ArrayList<Summand> summands;

    public Factor(Summand main, ArrayList<String> ops, ArrayList<Summand> summands) {
        this.main = main;
        this.ops = ops;
        this.summands = summands;
    }

    public Factor(HashMap<String, Object> map) {
        this.main = new Summand((HashMap<String, Object>) map.get("left"));
    }

    public String toJavaCode() {
        StringBuilder result = new StringBuilder(main.toJavaCode());
        for (String op: ops
             ) {
            result.append(op).append(summands.remove(0).toJavaCode());
        }
        return result.toString();
    }
}
