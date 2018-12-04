package Generator.Entities;

public class Summand {
    private Primary mainPrimary;
    private Expression mainExpression;

    public Summand(Primary mainPrimary) {
        this.mainPrimary = mainPrimary;
    }

    public Summand(Expression mainExpression) {
        this.mainExpression = mainExpression;
    }

    public Expression getMainExpression() {
        return mainExpression;
    }

    public Primary getMainPrimary() {
        return mainPrimary;
    }
}
