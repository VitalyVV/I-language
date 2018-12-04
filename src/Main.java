import Lexer.Lexer;
import Syntax.SyntaxParser;
import Syntax.WrongSyntaxException;

import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws NotActiveException, FileNotFoundException, WrongSyntaxException {
        Lexer lx = new Lexer();
        Scanner sc = new Scanner(new File("in.txt"));
        StringBuilder s = new StringBuilder();

        while(sc.hasNextLine()){
            s.append(sc.nextLine());
            s.append("\n");
        }
        lx.parseString(s.toString());
        SyntaxParser sp = new SyntaxParser(lx.getTokens());
        SemanticAnalyzer ss = new SemanticAnalyzer();
        try {
            ss.analyze(sp.getRoot());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
