package Generator.Entities;

import java.util.ArrayList;

public class ModifiablePrimary {
    private Identifier main;
    private ArrayList<Identifier> identifiers;
    private ArrayList<Expression> expressions;
    private ArrayList<String> mod;

    public ModifiablePrimary(Identifier main, ArrayList<Identifier> identifiers, ArrayList<Expression> expression, ArrayList<String> mod) {
        this.main = main;
        this.identifiers = identifiers;
        this.expressions = expression;
        this.mod = mod;
    }

    public String toJavaCode() {
        StringBuilder result = new StringBuilder(main.toJavaCode());
        for (String mod : mod
                ) {
            if (mod.equals(".")) {
                result.append(".").append(identifiers.remove(0).toJavaCode());
            }
            else if (mod.equals("[]")) {
                result.append("[").append(expressions.remove(0).toJavaCode()).append("]");
            }
        }
        return result.toString();
    }
}
