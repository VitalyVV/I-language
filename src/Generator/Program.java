package Generator;

import Generator.Declarations.Declaration;

import java.util.ArrayList;

public class Program {
    private ArrayList<Declaration> program = new ArrayList<Declaration>();

    Program (ArrayList<Declaration> declarations) {
        program.addAll(declarations);
    }

    ArrayList<Declaration> getProgram() {
        return program;
    }
}
