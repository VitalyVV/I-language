package Generator.Statements;

import Generator.Declarations.Body;
import Generator.Entities.Expression;

public class IfStatement extends Statement {
    private Expression ifExpression;
    private Body thenBody;
    private Body elseBody;

    public IfStatement(Expression expression, Body body, Body elseBody) {
        this.ifExpression = expression;
        this.thenBody = body;
        this.elseBody = elseBody;
    }

    public String toJavaCode() {
        String controlFlow = "if( " + ifExpression.toJavaCode() + ")";
        String result = controlFlow + "{ " + thenBody.toJavaCode() + "}";
        if (elseBody != null) {
            result += "else{ " + elseBody.toJavaCode() + "}";
        }
        return result;
    }
}
