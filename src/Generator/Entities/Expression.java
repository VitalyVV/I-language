package Generator.Entities;

import java.util.ArrayList;

public class Expression {
    private Relation main;
    private String op;
    private ArrayList<Relation> relations;

    public Expression(Relation main, String op, ArrayList<Relation> relations) {
        this.main = main;
        this.op = op;
        this.relations = relations;
    }

    public Relation getMain() {
        return main;
    }

    public String getOp() {
        return op;
    }

    public ArrayList<Relation> getRelations() {
        return relations;
    }
}
