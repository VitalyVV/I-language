package Generator.Entities;

import java.util.ArrayList;

public class ModifiablePrimary {
    private Identifier main;
    private ArrayList<Identifier> identifiers;
    private Expression expression;

    public ModifiablePrimary(Identifier main) {
        this.main = main;
    }
}
