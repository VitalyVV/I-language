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

    public Identifier getMain() {
        return main;
    }

    public ArrayList<Identifier> getIdentifiers() {
        return identifiers;
    }

    public ArrayList<Expression> getExpressions() {
        return expressions;
    }

    public ArrayList<String> getMod() {
        return mod;
    }
}
