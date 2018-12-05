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

    public Relation getMain() {
        return main;
    }

    public ArrayList<String> getOps() {
        return ops;
    }

    public ArrayList<Relation> getRelations() {
        return relations;
    }
}
