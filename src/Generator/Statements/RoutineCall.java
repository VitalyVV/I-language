package Generator.Statements;

import Generator.Entities.Expression;
import Generator.Entities.Identifier;

import java.util.ArrayList;

public class RoutineCall extends Statement {
    private Identifier identifier;
    private ArrayList<Expression> expressions;

    public RoutineCall(Identifier identifier, ArrayList<Expression> expressions) {
        this.identifier = identifier;
        this.expressions = expressions;
    }

    public String toJavaCode() {
        StringBuilder result = new StringBuilder(identifier.toJavaCode());
        for (int i = 0; i < expressions.size(); i++) {
            if (i == 0) {
                result.append(expressions.get(0));
            } else if (i == expressions.size() - 1) {
                result.append(expressions.get(i));
            } else {
                result.append(expressions.get(i)).append(",");
            }
        }
        return result.toString();
    }
}
