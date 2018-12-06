package Generator.Entities;

import java.util.ArrayList;
import java.util.HashMap;

public class Relation {
    private Simple main;
    private ArrayList<String> ops;
    private ArrayList<Simple> terms;

    public Relation(HashMap<String, Object> map) {
        this.main = new Simple((HashMap<String, Object>) map.get("left"));
        this.ops = ops;
        this.terms = terms;
    }

    public String toJavaCode() {
        StringBuilder result = new StringBuilder(main.toJavaCode());
        for (String op : ops
                ) {
            if (op.equals("=")) op = "==";
            if (op.equals("/=")) op = "!=";
            result.append(op).append(terms.remove(0).toJavaCode());
        }
        return result.toString();
    }
}
