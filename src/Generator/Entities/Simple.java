package Generator.Entities;

import java.util.ArrayList;

public class Simple {
    private Factor main;
    private ArrayList<String> ops;
    private ArrayList<Factor> factors;

    public Simple(Factor main, ArrayList<String> ops, ArrayList<Factor> factors) {
        this.main = main;
        this.ops = ops;
        this.factors = factors;
    }

    public ArrayList<String> getOps() {
        return ops;
    }

    public Factor getMain() {
        return main;
    }

    public ArrayList<Factor> getFactors() {
        return factors;
    }
}
