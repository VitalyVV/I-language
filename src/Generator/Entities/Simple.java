package Generator.Entities;

import java.util.ArrayList;

public class Simple {
    private Factor main;
    private String op;
    private ArrayList<Factor> factors;

    public Simple(Factor main, String op, ArrayList<Factor> factors) {
        this.main = main;
        this.op = op;
        this.factors = factors;
    }

    public String getOp() {
        return op;
    }

    public Factor getMain() {
        return main;
    }

    public ArrayList<Factor> getFactors() {
        return factors;
    }
}
