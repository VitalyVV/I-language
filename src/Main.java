import Lexer.Lexer;
import Syntax.SyntaxParser;
import Syntax.TreeTranslator;
import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Lexer lx = new Lexer();
        Scanner sc = new Scanner(new File("in.txt"));
        StringBuilder s = new StringBuilder();

        while(sc.hasNextLine()){
            s.append(sc.nextLine());
            s.append("\n");
        }
        lx.parseString(s.toString());
        SyntaxParser sp = new SyntaxParser(lx.getTokens());

        TreeTranslator translator = new TreeTranslator(sp.getTree());
        translator.translate();
        SemanticAnalyzer ss = new SemanticAnalyzer();

        try {
            ss.analyze(sp.getRoot());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
