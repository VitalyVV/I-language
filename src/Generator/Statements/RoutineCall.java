package Generator.Statements;

import Generator.Entities.Expression;
import Generator.Entities.Identifier;

import java.util.ArrayList;

public class RoutineCall extends Statement {
    private Identifier identifier;
    private ArrayList<Expression>  expressions;

    public RoutineCall(Identifier identifier, ArrayList<Expression> expressions) {
        this.identifier = identifier;
        this.expressions = expressions;
    }

    public ArrayList<Expression> getExpressions() {
        return expressions;
    }

    public Identifier getIdentifier() {
        return identifier;
    }
}
