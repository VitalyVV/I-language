import Lexer.Lexer;
import Syntax.SyntaxParser;
import Syntax.WrongSyntaxException;

import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws NotActiveException, FileNotFoundException, WrongSyntaxException {
        Lexer lx = new Lexer();
        Scanner sc = new Scanner(new File("in.txt"));
        String s = "";

        while(sc.hasNextLine()){
            s+=sc.nextLine();
            s+="\n";
        }
        lx.parseString(s);
        SyntaxParser sp = new SyntaxParser(lx.getTokens());

    }
}
