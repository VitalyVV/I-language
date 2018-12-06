package Generator.Entities;

import java.util.ArrayList;
import java.util.HashMap;

public class Simple {
    private Factor main;
    private ArrayList<String> ops;
    private ArrayList<Factor> factors;

    public Simple(HashMap<String, Object> map) {
        this.main = new Factor((HashMap<String, Object>) map.get("left"));
        this.ops = ops;
        this.factors = factors;
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
