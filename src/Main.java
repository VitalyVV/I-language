import Lexer.Lexer;

import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws NotActiveException, FileNotFoundException {
        Lexer lx = new Lexer();
        Scanner sc = new Scanner(new File("in.txt"));
        String s = "";

        while(sc.hasNextLine()){
            s+=sc.nextLine();
        }
        lx.parseString(s);

        while(lx.hasNextToken()){
            System.out.println(lx.getNextToken());
        }

//        SemanticAnalyzer sa = new SemanticAnalyzer();
//        sa.check();
    }
}
