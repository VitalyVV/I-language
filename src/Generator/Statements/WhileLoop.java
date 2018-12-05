package Generator.Statements;

import Generator.Declarations.Declaration;
import Generator.Entities.Expression;

public class WhileLoop extends Statement {
    private Expression expression;
    private Declaration.Body body;

    public WhileLoop(Expression expression, Declaration.Body body) {
        this.expression = expression;
        this.body = body;
    }

    public Expression getExpression() {
        return expression;
    }

    public Declaration.Body getBody() {
        return body;
    }
}
