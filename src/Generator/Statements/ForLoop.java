package Generator.Statements;

import Generator.Declarations.Body;
import Generator.Entities.Identifier;
import Generator.Declarations.Range;

public class ForLoop extends Statement {
    private Identifier runner;
    private boolean reverse;
    private Range range;
    private Body body;

    public ForLoop(Identifier runner, boolean reverse, Range range, Body body) {
        this.runner = runner;
        this.reverse = reverse;
        this.range = range;
        this.body = body;
    }

    public String toJavaCode() {
        String controlFlow;
        if (reverse)
            controlFlow = "for (" + runner.toJavaCode() + "=" + range.getFrom().toJavaCode() + ";" +
                    runner.toJavaCode() + ">" + range.getTo().toJavaCode() + ";" + runner.toJavaCode() + "--" + ")";
        else controlFlow = "for (" + runner.toJavaCode() + "=" + range.getFrom().toJavaCode() + ";" +
                runner.toJavaCode() + "<" + range.getTo().toJavaCode() + ";" + runner.toJavaCode() + "++" + ")";
        return controlFlow + "{" + body.toJavaCode() + "}";
    }
}


