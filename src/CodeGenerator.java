import generator.*;

import java.util.HashMap;

public class CodeGenerator {
    public String generateCode(HashMap<String,Object> tree) {
        ProgramGenerator pg = new ProgramGenerator(tree);
        return pg.getCode();
    }
}