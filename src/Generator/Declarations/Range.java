package Generator.Declarations;

import Generator.Entities.Expression;

public class Range {
    private Expression from;
    private Expression to;

    public Range(Expression from, Expression to) {
        this.from = from;
        this.to = to;
    }

    public Expression getTo() {
        return to;
    }

    public Expression getFrom() {
        return from;
    }
}
