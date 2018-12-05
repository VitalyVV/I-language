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

    public Simple getMain() {
        return main;
    }

    public ArrayList<String> getOps() {
        return ops;
    }

    public ArrayList<Simple> getTerms() {
        return terms;
    }
}
