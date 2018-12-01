package Syntax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Pattern;

public class SyntaxParser {

    private HashMap<String, Object> tree = new HashMap<>();
    private ArrayList<String> tokens;
    private String keyword = "";
    private String buff = "";
    private int index = 0;

    public SyntaxParser(ArrayList<String> tokens){
        this.tokens = tokens;
        this.parseProgram();
    }

    private void flush(Object data, String sec){
        if (tree.isEmpty()){
            tree.put("Root", data);
        }else{
            tree.put("Section", sec);
            tree.put("Content", data);
        }
    }

    private String nextWord(){
        String word = tokens.get(index);
        ++index;
        return word;
    }

    private void parseProgram(){
        if (!parseSimpleDeclaration())
            parseRoutineDeclaration();
    }

    private boolean parseSimpleDeclaration(){
        if (!parseVariableDeclaration())
            parseTypeDeclaration();
    }

    private boolean parseRoutineDeclaration(){

    }

    private boolean parseVariableDeclaration(){
        keyword = "";
        HashMap<String, Object> obj = new HashMap<>();
        obj.put("Section", "Statement");
        HashMap<String, String> temp = new HashMap<String, String>();
        String word = nextWord();
        if (word.equals("var")){
            temp.put("statement", "var");
            word = nextWord();
            if (parseIdentifier(word)){
                temp.put("name", word);
                word = nextWord();
                if (word.equals("col")){
                    if (parseType()) {
                        temp.put("type", buff);
                        word = nextWord();
                        if (word.equals("is")){
                            if (parseExpression()){
                                obj.put("Content", temp);
                            }else return false;
                        }else return false;
                    }else return false;
                }else if(word.equals("is")){
                    if (parseExpression()){
                    }else return false;
                }else return false;
            }else return false;
        }else return false;
    }

    private boolean parseTypeDeclaration (){

    }

    private boolean parseType (){
        String word = nextWord();
        if (!parsePrimitiveType()){
            if(!parseUserType()){
                if(parseIdentifier(word)){
                    buff = word;
                    return true;
                }else return false;
            }else {
                buff = word;
                return true;
            }
        }else{
            buff = word;
            return true;
        }
    }


    private boolean parsePrimitiveType (){

    }

    private boolean parseUserType (){

    }

    private boolean parseRecordType (){

    }

    private boolean parseArrayType (){

    }

    private boolean parseStatement (){

    }

    private boolean parseAssignment (){

    }

    private boolean parseRoutineCall (){

    }

    private boolean parseWhileLoop (){

    }

    private boolean parseForLoop (){

    }

    private boolean parseRange (){

    }

    private boolean parseIfStatement (){

    }

    private boolean parseParameters (){

    }
    private boolean parseParameterDeclaration (){

    }

    private boolean parseBody (){

    }

    private boolean parseExpression(){
        return true;
    }

    private boolean parseIdentifier(String word){
        Pattern p = Pattern.compile("^[a-zA-Z_$][a-zA-Z_$0-9]*$");
        return p.matcher(word).matches();
    }


}
