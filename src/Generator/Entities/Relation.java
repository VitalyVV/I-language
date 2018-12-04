package Generator.Entities;

import java.util.ArrayList;

public class Relation {
    private Simple main;
    private String op;
    private ArrayList<Simple> terms;

    public Relation(Simple main, String op, ArrayList<Simple> terms) {
        this.main = main;
        this.op = op;
        this.terms = terms;
    }

    public Simple getMain() {
        return main;
    }

    public String getOp() {
        return op;
    }

    public ArrayList<Simple> getTerms() {
        return terms;
    }
}
