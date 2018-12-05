package Generator.Entities;

import java.util.ArrayList;

public class Expression {
    private Relation main;
    private ArrayList<String> ops;
    private ArrayList<Relation> relations;

    public Expression(Relation main, ArrayList<String> ops, ArrayList<Relation> relations) {
        this.main = main;
        this.ops = ops;
        this.relations = relations;
    }

    public String toJavaCode() {
        StringBuilder result = new StringBuilder(main.toJavaCode());
        for (String op : ops
                ) {
            switch (op) {
                case "and": {
                    result.append(" && ").append(relations.remove(0).toJavaCode());
                }
                case "or": {
                    result.append(" || ").append(relations.remove(0).toJavaCode());
                }
                case "xor": {
                    result.append(" ^ ").append(relations.remove(0).toJavaCode());
                }
                default:
                    break;
            }
        }
        return result.toString();
    }
}
