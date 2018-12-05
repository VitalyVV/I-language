package Generator.Statements;

import Generator.Declarations.Declaration;
import Generator.Entities.Identifier;
import Generator.Declarations.Range;

public class ForLoop {
    private Identifier runner;
    private boolean reverse;
    private Range range;
    private Declaration.Body body;

    public ForLoop(Identifier runner, boolean reverse, Range range, Declaration.Body body) {
        this.runner = runner;
        this.reverse = reverse;
        this.range = range;
        this.body = body;
    }

    public Declaration.Body getBody() {
        return body;
    }

    public Range getRange() {
        return range;
    }

    public boolean isReverse() {
        return reverse;
    }

    public Identifier getRunner() {
        return runner;
    }
}
