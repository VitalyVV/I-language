package Lexer;

import java.io.NotActiveException;
import java.util.*;

public class Lexer {

    private Map<String, String> lexems = new HashMap<>();
    private ArrayList<String> tokens = new ArrayList<>();
    private boolean isSet = false;

    public Lexer(){
        lexems.put(".", "dot");
        lexems.put("=", "eq");
        lexems.put("<", "less");
        lexems.put(">", "great");
        lexems.put("not", "not");
        lexems.put("and", "and");
        lexems.put("or", "or");
        lexems.put("<=", "lesseq");
        lexems.put(">=", "greateq");
        lexems.put("var", "var");
        lexems.put("is", "is");
        lexems.put("type", "type");
        lexems.put("integer", "int");
        lexems.put("real", "real");
        lexems.put("boolean", "bool");
        lexems.put("true", "true");
        lexems.put("false", "false");
        lexems.put("record", "rec");
        lexems.put("end", "end");
        lexems.put("array", "array");
        lexems.put(":=", "ass");
        lexems.put(":", "col");
        lexems.put("(", "lbr");
        lexems.put(")", "rbr");
        lexems.put("[", "lsbr");
        lexems.put("]", "rsbr");
        lexems.put("{", "lfbr");
        lexems.put("}", "rfbr");
        lexems.put(",", "comm");
        lexems.put(";", "semicol");
        lexems.put("while", "while");
        lexems.put("loop", "loop");
        lexems.put("in", "in");
        lexems.put("reverse", "revers");
        lexems.put("if", "if");
        lexems.put("then", "then");
        lexems.put("else", "else");
        lexems.put("routine", "routine");
        lexems.put("/=", "noteq");
        lexems.put("*", "mul");
        lexems.put("/", "div");
        lexems.put("%", "perc");
        lexems.put("+", "add");
        lexems.put("-", "sub");
        lexems.put("\"", "quot");
    }


    public void parseString(String input){
        StringTokenizer tokenizer = new StringTokenizer(input);
        while(tokenizer.hasMoreTokens()){
            String current = tokenizer.nextToken();
            FSM fsm = new FSM(current);
            fsm.parse();
            ArrayList<String> temp = fsm.getTokens();
            adjust(temp);
        }
        for (int i = 0; i < tokens.size(); i++) {
            if (lexems.containsKey(tokens.get(i))){
                tokens.set(i, lexems.get(tokens.get(i)));
            }
        }
        isSet = true;
    }


    private void adjust(ArrayList<String> list){
        for (int i = 0; i < list.size(); i++) {
            tokens.add(list.get(i));
        }
    }


    private void isSetOrError() throws NotActiveException {
        if (!isSet){
            throw new NotActiveException("Lexer.Lexer should be initialized.");
        }
    }

    public String getNextToken(){
        return tokens.remove(0);
    }

    public ArrayList<String> getTokens() {
        return tokens;
    }

    public boolean hasNextToken() throws NotActiveException {
        isSetOrError();
        return !tokens.isEmpty();
    }



}
