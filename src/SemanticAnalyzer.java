import helpers.*;

import java.util.*;

public class SemanticAnalyzer{

    public void analyze(ArrayList<HashMap<String, Object>> toAst) throws Exception{
        ProgramNode program = new ProgramNode("Program", toAst);
        program.createSymbols();
    }
}