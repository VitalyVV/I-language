package Generator.Entities;

import java.util.ArrayList;

public class Relation {
    private Simple main;
    private ArrayList<String> ops;
    private ArrayList<Simple> terms;

    public Relation(Simple main, ArrayList<String> ops, ArrayList<Simple> terms) {
        this.main = main;
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
