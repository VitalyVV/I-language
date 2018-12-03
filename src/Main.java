import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
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

        SemanticAnalyzer ss = new SemanticAnalyzer();
        HashMap<String, Object> map = new HashMap<>();
        map.put("statement", "var");
        map.put("name", "a");
        map.put("hastype", "true");

        HashMap<String, Object> type = new HashMap<>();
        type.put("primitive", "int");
        map.put("type", type);
        HashMap<String, Object> subtree = new HashMap<>();
        subtree.put("Content",map);


        ArrayList<HashMap<String, Object>> tree = new ArrayList<>();
        tree.add(subtree);


        ss.analyze(tree);

    }
}
