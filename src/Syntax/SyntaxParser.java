package Syntax;

import javax.xml.ws.Action;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Pattern;

public class SyntaxParser {

    private HashMap<String, Object> tree = new HashMap<>();
    private ArrayList<String> tokens;
    private Object buff = "";
    private int index = 0;
    private String[] keywords = {"and", "or", "not", "and",
            "or", "var", "is", "type",
            "integer", "real", "boolean",
            "true", "false", "record",
            "end", "array", "while",
            "loop", "in", "reverse",
            "if", "then", "else",
            "routine", "not"
    };

    public SyntaxParser(ArrayList<String> tokens){
        this.tokens = tokens;
        tree.put("Root", this.parseProgram());
    }

    private void flush(Object data){
        if (tree.isEmpty()){
            tree.put("Root", data);
        }else{
            tree.put("Content", data);
        }
    }

    private String nextWord(){
        String word = "";
        if(index + 1 < tokens.size()){
            word = tokens.get(index);
            ++index;
        }
        return word;
    }

    private ArrayList<HashMap<String, Object>> parseProgram(){
        ArrayList<HashMap<String, Object>> content = new ArrayList<>();
        String word = nextWord();
        while(index<tokens.size()){
            HashMap<String, Object> temp = new HashMap<>();
            if(word.equals("routine")){
                temp.put("Section", "Statement");
                temp.put("Content", parseRoutineDeclaration());
            }else{
                --index;
                temp.put("Section", "Statement");
                temp.put("Content", parseSimpleDeclaration());
            }
            content.add(temp);
            word = nextWord();
        }
        return content;

    }

    private HashMap<String, Object> parseSimpleDeclaration(){
        HashMap<String, Object> temp = new HashMap<>();
        String word = nextWord();
        if(word.equals("var")){
            temp = parseVariableDeclaration();
        }else if(word.equals("type")){
            temp = parseTypeDeclaration();
        }
        return temp;
    }

    private HashMap<String, Object> parseRoutineDeclaration(){
        HashMap<String, Object> temp = new HashMap<>();
        String word = nextWord();
        temp.put("statement", "declaration");
        if(parseIdentifier(word)){
            temp.put("name", word);
            word = nextWord();
            if(word.equals("lbr")){
                temp.put("parameters", parseParameters());
                word = nextWord();
            }
            if(word.equals("col")){
                temp.put("hastype", "true");
                temp.put("type", parseType());
                word = nextWord();
            }else{
                temp.put("hastype", "false");
                temp.put("type", "None");
            }
            if(word.equals("is")){
                temp.put("hasbody", "true");
                temp.put("body", parseBody());
            }else{
                temp.put("hasbody", "false");
                temp.put("body", "None");
            }
        } //Todo: maybe error?
        return temp;
    }

    private HashMap<String, Object> parseVariableDeclaration(){
        HashMap<String, Object> temp = new HashMap<String, Object>();
        String word = nextWord();
        temp.put("statement", "var");

        if (parseIdentifier(word)) {
            temp.put("name", word);
            word = nextWord();

            if (word.equals("col")) {
                temp.put("hastype", "true");
                temp.put("type", parseType());
                word = nextWord();
                if (word.equals("is")) {

                    temp.put("value", parseExpression());
                    return temp;

                } else {
                    --index;
                }
            } else if (word.equals("is")) {
                temp.put("hastype", "false");
                temp.put("type", "None");
                temp.put("expression", parseExpression());
            } //TODO :Maybe error?
        } //TODO :Maybe error?

    return temp;
    }

    private HashMap<String, Object> parseTypeDeclaration (){
        HashMap<String, Object> temp = new HashMap<String, Object>();
        String word = nextWord();
        temp.put("statement", "type");

        if(parseIdentifier(word)){
            temp.put("name", word);
            word = nextWord();

            if (word.equals("is")){
                    temp.put("type", parseType());
            }
        }
        return temp;
    }

    private HashMap<String, Object> parseType (){
        HashMap<String, Object> temp = new HashMap<String, Object>();
        String word = nextWord();
        if (word.equals("integer") || word.equals("real") || word.equals("boolean")){
            //parsePrimitiveType()
            temp.put("primitive", word);
        }else if(word.equals("array")){
            temp.put("array",parseArrayType());
        }else if (word.equals("record")) {
            temp.put("record", parseRecordType());
        }else {
            if (parseIdentifier(word)){
                temp.put("identifier", word);
            } // TODO: Maybe error
        }
        return temp;
    }


    private HashMap<String, Object> parseRecordType (){
        HashMap<String, Object> temp = new HashMap<>();
        String word = nextWord();
        int N = 0;

        ArrayList<HashMap<String, Object>> content = new ArrayList<>();
        while(!word.equals("end")){
            HashMap<String, Object> temp2 = parseVariableDeclaration();
            content.add(temp2);
            word = nextWord();
            ++N;
        }
        temp.put("content", content);
        temp.put("N", Integer.toString(N));
        return temp;
    }

    private HashMap<String, Object> parseArrayType () {
        HashMap<String, Object> temp = new HashMap<>();
        String word = nextWord();

        if (word.equals("lsbr")) {
            temp.put("length", parseExpression());
            word = nextWord();
            if (word.equals("rsbr")) {
                temp.put("type", parseType());
            }
        }
        return temp;
    }


    private HashMap<String, Object> parseStatement (){
        HashMap<String, Object> temp = new HashMap<>();
        temp.put("Section", "Statement");

        String word = nextWord();
        if(parseIdentifier(word)){
            String op = nextWord();
            if(op.equals("ass") || op.equals("dot") || op.equals("lsbr")){
                --index;
                --index;
                temp.put("Content", parseAssignment());
            }else {
                if(!op.equals("lbr")){
                    --index;
                }
                temp.put("Content", parseRoutineCall(word));
            }
        }else if(word.equals("while")){
            temp.put("Content", parseWhileLoop());
        }else if(word.equals("for")){
            temp.put("Content", parseForLoop());
        }else if(word.equals("if")){
            temp.put("Content", parseIfStatement());

        } // TODO: maybe error?

        return temp;
    }

    private HashMap<String, Object> parseAssignment (){
        HashMap<String, Object> temp = new HashMap<>();
        temp.put("statement", "assignment");
        temp.put("name", parseModifiablePrimary());
        temp.put("value", parseExpression());
        return temp;
    }

    private HashMap<String, Object> parseRoutineCall (String name){
        HashMap<String, Object> temp = new HashMap<>();
        temp.put("statement", "call");
        temp.put("variable", name);
        String word = nextWord();
        if(word.equals("lbr")){
            ArrayList<HashMap<String, Object>> params = new ArrayList<>();
            params.add(parseExpression());
            word = nextWord();
            if(word.equals("comm")){
                while (!word.equals("rbr")){
                    params.add(parseExpression());
                    word = nextWord();
                    if(!word.equals("comm")){
                        //TODO: maybe error?
                    }
                }
            }
            temp.put("parameters", params);
        }else{
            --index;
        }

        return temp;
    }

    private HashMap<String, Object> parseWhileLoop (){
        HashMap<String, Object> temp = new HashMap<>();
        String word = nextWord();
        temp.put("statement", "while");
        temp.put("expression", parseExpression());
        if(word.equals("loop")){
            temp.put("body", parseBody());
        } // TODO: maybe error?

        return temp;
    }

    //"for" Identifier "in" ["reverse"] Range "loop" Body "end"
    private HashMap<String, Object> parseForLoop (){
        HashMap<String, Object> temp = new HashMap<>();
        String word = nextWord();
        temp.put("statement", "for");
        if(parseIdentifier(word)){
            temp.put("identifier", word);
            word = nextWord();
            if(word.equals("in")){
                word = nextWord();
                temp.put("reverse", word.equals("reverse"));
                temp.put("range", parseRange());
                word = nextWord();
                if(word.equals("loop")){
                    temp.put("body", parseBody());

                }//TODO: maybe error?
            }//TODO maybe error?
        } //TODO: maybe error?
        return temp;
    }

    //Range : Expression ".." Expression
    private HashMap<String, Object> parseRange (){
        HashMap<String, Object> temp = new HashMap<>();
        String word = nextWord();
        temp.put("expression_from", parseExpression());
        if(word.equals("doubdot")){
            temp.put("expression_to", parseExpression());
        }
        return temp;
    }

    //IfStatement : "if" Expression "then" Body ["else" Body] "end"
    private HashMap<String, Object> parseIfStatement (){
        HashMap<String, Object> temp = new HashMap<>();
        temp.put("statement", "if");
        temp.put("expression", parseExpression());
        String word = nextWord();
        if(word.equals("then")){
            temp.put("body", parseBody());
            if(tokens.get(index).equals("else")){
                temp.put("else", "true");
                temp.put("else_body", parseBody());
            }else{
                temp.put("else", "false");
            }
        }
        return temp;
    }

    private ArrayList<HashMap<String, Object>> parseParameters (){
        ArrayList<HashMap<String, Object>> params = new ArrayList<>();
        String word = nextWord();
        params.add(parseParameterDeclaration(word));
        while(!word.equals("rbr")){
            params.add(parseParameterDeclaration(word));
            word = nextWord();
        }
        return params;
    }

    private HashMap<String, Object> parseParameterDeclaration (String word){
        HashMap<String, Object> temp = new HashMap<>();
        if(parseIdentifier(word)){
            temp.put("name", word);
            String nxt = nextWord();
            if(nxt.equals("col")){
                temp.put("type", parseType());
            }
        }
        return temp;
    }

    private ArrayList<HashMap<String, Object>> parseBody (){
        ArrayList<HashMap<String, Object>> content = new ArrayList<>();
        String word = tokens.get(index);
        while(!word.equals("end") && !word.equals("else")){
            content.add(parseStatement());
            word = nextWord();
        }
        return content;
    }

    //Expression : Relation { ("and" | "or" | "xor") } Relation
    private HashMap<String, Object> parseExpression(){
        HashMap<String, Object> temp = new HashMap<>();
        temp.put("left", parseRelation());
        temp.put("is", "expression");
        String word = nextWord();
        if(word.equals("and") || word.equals("or") || word.equals("xor")){
            temp.put("hasright", "true");
            temp.put("op", word);
            temp.put("right", parseRelation());
            HashMap<String, Object> out = new HashMap<>();
            word = nextWord(); //TODO Maybe temporary
            while(word.equals("and") || word.equals("or") || word.equals("xor")){
                out.put("left", parseRelation());
                out.put("right", new HashMap<String, Object>(temp));
                out.put("op", word);
                temp = out;
                word = nextWord();
            }
        }else{
            if (index < tokens.size() && (!isOp(tokens.get(index)))){
                --index;
            }
            temp.put("hasright", "false");
        }

        return temp;
    }


    //Relation : Simple [ ( "less" | "lesseq" | "great" | "greateq" | "eq" | "noteq" ) Simple ]
    private HashMap<String, Object> parseRelation(){
        HashMap<String, Object> temp = new HashMap<>();
        temp.put("left", parseSimple());
        temp.put("is", "relation");
        String word = nextWord();
        if(isRelationSign(word)){
            temp.put("hasright", "true");
            temp.put("op", word);
            temp.put("right", parseSimple());
            HashMap<String, Object> out = new HashMap<>();
            word = nextWord(); //TODO Maybe temporary
            while(word.equals("and") || word.equals("or") || word.equals("xor")){
                out.put("left", parseSimple());
                out.put("right", new HashMap<String, Object>(temp));
                out.put("op", word);
                temp = out;
                word = nextWord();
            }
        }else{
            if (index < tokens.size() && (!isOp(tokens.get(index)))){
                --index;
            }
            temp.put("hasright", "false");
        }

        return temp;
    }

    private boolean isOp(String word){
        return word.equals("less")      || word.equals("lesseq") || word.equals("great") ||
                word.equals("greateq")  || word.equals("eq")     || word.equals("noteq") ||
                word.equals("and")      || word.equals("or")     || word.equals("xor")   ||
                word.equals("mul")      || word.equals("div")    || word.equals("perc")  ||
                word.equals("add")      || word.equals("sub")    || word.equals("not");
    }

    private boolean isRelationSign(String word){
        return word.equals("less") || word.equals("lesseq") || word.equals("great") ||
                word.equals("greateq") || word.equals("eq") || word.equals("noteq");
    }

    //Simple : Factor { ( "mul" | "div" | "perc" ) Factor }
    private HashMap<String, Object> parseSimple (){
        HashMap<String, Object> temp = new HashMap<>();
        temp.put("left", parseFactor());
        temp.put("is", "simple");
        String word = nextWord();
        if(word.equals("mul") || word.equals("div") || word.equals("perc")){
            ArrayList<HashMap<String, Object>> rigthpart = new ArrayList<>();
            temp.put("hasright", "true");
            temp.put("op", word);
            temp.put("right", parseFactor());
            HashMap<String, Object> out = new HashMap<>();
            word = nextWord(); //TODO Maybe temporary
            while(word.equals("and") || word.equals("or") || word.equals("xor")){
                out.put("left", parseFactor());
                out.put("right", new HashMap<String, Object>(temp));
                out.put("op", word);
                temp = out;
                word = nextWord();
            }
        }else{
            if (index < tokens.size() && (!isOp(tokens.get(index)))){
                --index;
            }
            temp.put("hasright", "false");
        }

        return temp;
    }

    // Factor : Summand { ( "add" | "sub" ) Summand }
    private HashMap<String, Object> parseFactor (){
        HashMap<String, Object> temp = new HashMap<>();
        temp.put("left", parseSummand());
        String word = nextWord();
        temp.put("is", "factor");
        if(word.equals("add") || word.equals("sub")){
            temp.put("hasright", "true");
            temp.put("op", word);
            temp.put("right", parseSummand());
            HashMap<String, Object> out = new HashMap<>();
            word = nextWord(); //TODO Maybe temporary
            while(word.equals("and") || word.equals("or") || word.equals("xor")){
                out.put("left", parseSummand());
                out.put("right", new HashMap<String, Object>(temp));
                out.put("op", word);
                temp = out;
                word = nextWord();
            }
        }else{
            if (index < tokens.size() && (!isOp(tokens.get(index)))){
                --index;
            }
            temp.put("hasright", "false");
        }

        return temp;
    }

    //Summand : Primary | "lbr" Expression "rbr"
    private HashMap<String, Object> parseSummand (){
        HashMap<String, Object> temp = new HashMap<>();
        String word = tokens.get(index);
        temp.put("is", "summand");
        if(word.equals("lbr")){
            temp.put("left", parseExpression());
            nextWord();
        }else{
            temp.put("left", parsePrimary());
        }

        return temp;
    }

    /*
    Primary : [ Sign | "not" ] IntegerLiteral
        | [ Sign ] RealLiteral
        | "true"
        | "false"
        | ModifiablePrimary
        | RoutineCall
     */
    private HashMap<String, Object> parsePrimary (){
        HashMap<String, Object> temp = new HashMap<>();
        String word = tokens.get(index);
        temp.put("is", "primary");
        if(word.equals("true") || word.equals("false")){
            temp.put("type", "boolean");
            temp.put("value", word);
        }else if(word.equals("add") || word.equals("sub") || word.equals("not")
                    || parseIntegerLiteral(word) || parseRealLiteral(word)) {

            if(word.equals("add") || word.equals("sub") || word.equals("not") ){
                temp.put("sign", word.equals("add")? "+": (word.equals("sub")? "-": "not"));
                if(word.equals("not")){
                    word = nextWord();
                    if(parseIntegerLiteral(word)){
                        temp.put("type", "integer");
                        temp.put("value", word);
                    }
                }else{
                    ++index;
                    word = nextWord();
                }
            }
            if(!(word.equals("add") || word.equals("sub"))){
                if(parseIntegerLiteral(word)){
                    temp.put("type", "integer");
                    temp.put("value", word);
                }else if(parseRealLiteral(word)){
                    temp.put("type", "real");
                    temp.put("value", word);
                }
            }
        }else if(parseIdentifier(word)){
            String op = nextWord();
            --index;
            if(op.equals("dot") || op.equals("lsbr")){
                temp.put("type", "modifiable");
                temp.put("value", parseModifiablePrimary());
            }else{
                temp.put("type", "routinecall");
                temp.put("value", parseRoutineCall(word));
            }
        }

        return temp;
    }

    private boolean parseIntegerLiteral(String word){
        Pattern p = Pattern.compile("[0-9]*");
        return p.matcher(word).matches();
    }

    private boolean parseRealLiteral(String word){
        Pattern p = Pattern.compile("[0-9]*.[0-9]*");
        return p.matcher(word).matches();
    }

    private boolean parseIdentifier(String word){
        Pattern p = Pattern.compile("^[a-zA-Z_$][a-zA-Z_$0-9]*$");
        return p.matcher(word).matches() && !(isKeyword(word));
    }

    //ModifiablePrimary: Identifier { "dot" Identifier | "lsbr" Expression "rsbr" }
    private HashMap<String, Object> parseModifiablePrimary(){
        HashMap<String, Object> temp = new HashMap<>();
        temp.put("statement", "modifiable");
        String word = nextWord();
        if(parseIdentifier(word)){
            temp.put("value", word);
            ArrayList<HashMap<String, Object>> chain = new ArrayList<>();
            word = nextWord();
            while(true){
                if(word.equals("dot") || word.equals("lsbr")){
                    HashMap<String, Object> temp2 = new HashMap<>();
                    if(word.equals("dot")){
                        temp2.put("type", "dot");
                        word = nextWord();
                        if(parseIdentifier(word)){
                            temp2.put("value", word);
                        }//TODO: maybe error?
                    }else{
                        temp2.put("type", "expression");
                        temp2.put("value", parseExpression());
                        nextWord();
                    }
                    chain.add(temp2);
                    word = nextWord();
                }else{
                    --index;
                    break;
                }

            }
            temp.put("mods", chain);
        }

        return temp;
    }

    private boolean isKeyword(String s){
        for (int i = 0; i < keywords.length; i++) {
            if(keywords[i].equals(s)){
                return true;
            }
        }
        return false;
    }

}
