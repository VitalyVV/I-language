import Lexer.Lexer;
import Syntax.SyntaxParser;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
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
        ArrayList<HashMap<String, Object>> oo = sp.getRoot();
        HashMap<String, Object> inn = (HashMap<String, Object>) oo.get(0);
        HashMap<String, Object> cont = (HashMap<String, Object>) inn.get("Content");
        HashMap<String, Object> contl = (HashMap<String, Object>) cont.get("value");

        SemanticAnalyzer ss = new SemanticAnalyzer();

        try {
            ss.analyze(sp.getRoot());
        } catch (Exception e) {
            e.printStackTrace();
       }

        String [] cmd ={"python", "translator.py"};
        ProcessBuilder pb = new ProcessBuilder (cmd);
        try{
            Process p = pb.start();
            p.waitFor();}
        catch (InterruptedException e){
            e.printStackTrace();}


        String [] cmd1 ={"python", "out.py"};
        ProcessBuilder pb1 = new ProcessBuilder (cmd1);
        try{
            Process p = pb1.start();
            p.waitFor();}
        catch (InterruptedException e){
            e.printStackTrace();}

    }
}
