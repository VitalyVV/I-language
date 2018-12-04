package Generator.Statements;

import Generator.Declarations.Declaration;
import Generator.Entities.Expression;

public class IfStatement {
    private Expression ifExpression;
    private Declaration.Body thenBody;
    private Declaration.Body elseBody;

    public IfStatement(Expression expression, Declaration.Body body, Declaration.Body elseBody) {
        this.ifExpression = expression;
        this.thenBody = body;
        this.elseBody = elseBody;
    }

    public Declaration.Body getElseBody() {
        return elseBody;
    }

    public Declaration.Body getThenBody() {
        return thenBody;
    }

    public Expression getIfExpression() {
        return ifExpression;
    }
}
