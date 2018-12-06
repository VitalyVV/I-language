package Generator.Statements;

import Generator.Declarations.Body;
import Generator.Entities.Expression;

public class WhileLoop extends Statement {
    private Expression expression;
    private Body body;

    public WhileLoop(Expression expression, Body body) {
        this.expression = expression;
        this.body = body;
    }

    public String toJavaCode() {
        String controlFlow = "while( " + expression.toJavaCode() + ")";
        return controlFlow + "{ " + body.toJavaCode() + "}";
    }
}
