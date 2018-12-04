package Generator.Entities;

import java.util.ArrayList;

public class Factor {
    private Summand main;
    private ArrayList<String> ops;
    private ArrayList<Summand> summands;

    public Factor(Summand main, ArrayList<String> ops, ArrayList<Summand> summands) {
        this.main = main;
        this.ops = ops;
        this.summands = summands;
    }

    public Summand getMain() {
        return main;
    }

    public ArrayList<String> getOps() {
        return ops;
    }

    public ArrayList<Summand> getSummands() {
        return summands;
    }
}
