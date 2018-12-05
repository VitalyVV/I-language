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

    public String toJavaCode() {
        StringBuilder result = new StringBuilder();
        if (mainExpression != null)
            return result.append("(").append(mainExpression.toJavaCode()).append(")").toString();
        return result.append(mainPrimary.toJavaCode()).toString();
    }
}
