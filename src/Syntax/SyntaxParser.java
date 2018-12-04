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
    private String[] keywords = {"and",
            "or", "var", "is", "type",
            "integer", "real", "boolean",
            "true", "false", "record",
            "end", "array", "while",
            "loop", "in", "reverse",
            "if", "then", "else",
            "routine", "not", "for"
    };

    public HashMap<String, Object> getTree() {
        return tree;
    }

    public ArrayList<HashMap<String, Object>> getRoot() {
        return (ArrayList<HashMap<String, Object>>) tree.get("Root");
    }

    public SyntaxParser(ArrayList<String> tokens) throws WrongSyntaxException {
        this.tokens = tokens;
        tree.put("Root", this.parseProgram());
    }

    private String nextWord() {
        String word = "";
        if (index + 1 < tokens.size()) {
            word = tokens.get(index);
            ++index;
        }
        return word;
    }

    private ArrayList<HashMap<String, Object>> parseProgram() throws WrongSyntaxException {
        ArrayList<HashMap<String, Object>> content = new ArrayList<>();
        String word = nextWord();
        while (index < tokens.size()) {
            HashMap<String, Object> temp = new HashMap<>();
            if(!word.equals("$$semicol")) {
                if (word.equals("$$routine")) {
                    temp.put("Section", "Statement");
                    temp.put("Content", parseRoutineDeclaration());
                } else if (word.equals("$$var") || word.equals("$$type")) {
                    --index;
                    temp.put("Section", "Statement");
                    temp.put("Content", parseSimpleDeclaration());
                } else if (word.equals("")) {
                    break;
                } else throw new WrongSyntaxException("Invalid syntax");

                if (temp.containsKey("Content")) {
                    content.add(temp);
                }
            }
            word = nextWord();
        }
        return content;

    }

    private HashMap<String, Object> parseSimpleDeclaration() throws WrongSyntaxException {
        HashMap<String, Object> temp = new HashMap<>();
        String word = nextWord();
        if (word.equals("$$var")) {
            temp = parseVariableDeclaration();
        } else if (word.equals("$$type")) {
            temp = parseTypeDeclaration();
        }
        return temp;
    }

    private HashMap<String, Object> parseRoutineDeclaration() throws WrongSyntaxException {
        HashMap<String, Object> temp = new HashMap<>();
        String word = nextWord();
        temp.put("statement", "declaration");
        if (parseIdentifier(word)) {
            temp.put("name", word.replaceAll("\\$", ""));
            word = nextWord();
            if (word.equals("$$lbr")) {
                temp.put("parameters", parseParameters());
                word = nextWord();
            }
            if (word.equals("$$col")) {
                temp.put("hastype", "true");
                temp.put("type", parseType());
                word = nextWord();
            } else {
                temp.put("hastype", "false");
                temp.put("type", "None");
            }
            if (word.equals("$$is")) {
                temp.put("hasbody", "true");
                temp.put("body", parseBody());
                ++index;
            } else {
                temp.put("hasbody", "false");
                temp.put("body", "None");
            }
        } else throw new WrongSyntaxException("Incorrect name for routine declaration. Got: " + word);
        return temp;
    }

    private HashMap<String, Object> parseVariableDeclaration() throws WrongSyntaxException {
        HashMap<String, Object> temp = new HashMap<String, Object>();
        String word = nextWord();
        temp.put("statement", "var");

        if (parseIdentifier(word)) {
            temp.put("name", word.replaceAll("\\$", ""));
            word = nextWord();
            if (word.equals("$$col")) {
                temp.put("hastype", "true");
                temp.put("type", parseType());
                if (((HashMap<String, Object>) temp.get("type")).containsKey("array")) {
                    return temp;
                }
                if (((HashMap<String, Object>) temp.get("type")).containsKey("record")) {
                    return temp;
                }
                word = nextWord();
                if (word.equals("$$is")) {

                    temp.put("value", parseExpression());
                    return temp;
                } else {
                    --index;
                }
            } else if (word.equals("$$is")) {
                temp.put("hastype", "false");
                temp.put("type", "None");
                temp.put("expression", parseExpression());
            } else{
                throw new WrongSyntaxException("Unexpected token: " + word + " ':' or 'is' expected in variable declaration.");
            }
        } else throw new WrongSyntaxException("Incorrect name for variable. Got: " + word);

        return temp;
    }

    private HashMap<String, Object> parseTypeDeclaration() throws WrongSyntaxException {
        HashMap<String, Object> temp = new HashMap<String, Object>();
        String word = nextWord();
        temp.put("statement", "type");

        if (parseIdentifier(word)) {
            temp.put("name", word.replaceAll("\\$", ""));
            word = nextWord();

            if (word.equals("$$is")) {
                temp.put("type", parseType());
            }
        }
        return temp;
    }

    private HashMap<String, Object> parseType() throws WrongSyntaxException {
        HashMap<String, Object> temp = new HashMap<String, Object>();
        String word = nextWord();
        if (word.equals("$$integer") || word.equals("$$real") || word.equals("$$boolean")) {
            //parsePrimitiveType()
            temp.put("primitive", word.replaceAll("\\$", ""));
        } else if (word.equals("$$array")) {
            temp.put("array", parseArrayType());
        } else if (word.equals("$$record")) {
            temp.put("record", parseRecordType());
            ++index;
        } else {
            if (parseIdentifier(word)) {
                temp.put("identifier", word.replaceAll("\\$", ""));
            } throw new WrongSyntaxException("Incorrect identifier for type declaration. Got: " + word);
        }
        return temp;
    }


    private HashMap<String, Object> parseRecordType() throws WrongSyntaxException {
        HashMap<String, Object> temp = new HashMap<>();
        String word = nextWord();
        int N = 0;

        ArrayList<HashMap<String, Object>> content = new ArrayList<>();
        while (!word.equals("$$end")) {
            HashMap<String, Object> temp2 = parseVariableDeclaration();
            content.add(temp2);
            word = nextWord();
            ++N;
            if (word.equals("")){
                throw new WrongSyntaxException("Unable to find exit from record");
            }
        }
        --index;
        temp.put("content", content);
        temp.put("N", Integer.toString(N));
        return temp;
    }

    private HashMap<String, Object> parseArrayType() throws WrongSyntaxException {
        HashMap<String, Object> temp = new HashMap<>();
        String word = nextWord();

        if (word.equals("$$lsbr")) {
            temp.put("length", parseExpression());
            word = nextWord();
            if (word.equals("$$rsbr")) {
                temp.put("type", parseType());
            }
        }
        return temp;
    }


    private HashMap<String, Object> parseStatement() throws WrongSyntaxException {
        HashMap<String, Object> temp = new HashMap<>();
        temp.put("Section", "Statement");
        String word = nextWord();
        if (parseIdentifier(word)) {
            String op = nextWord();
            --index;
            if (op.equals("$$lbr")) {
                temp.put("Content", parseRoutineCall(word));
            } else {
                temp.put("Content", parseAssignment());
            }
        } else if (word.equals("$$while")) {
            temp.put("Content", parseWhileLoop());
        } else if (word.equals("$$for")) {
            temp.put("Content", parseForLoop());
        } else if (word.equals("$$if")) {
            temp.put("Content", parseIfStatement());

        } else throw new WrongSyntaxException("Unexpected token " + word + ". Expect statement declaration, assignment or routine call.");

        return temp;
    }

    private HashMap<String, Object> parseAssignment() throws WrongSyntaxException {
        HashMap<String, Object> temp = new HashMap<>();
        temp.put("statement", "assignment");
        --index;
        temp.put("name", parseModifiablePrimary());
        ++index;
        temp.put("value", parseExpression());
        return temp;
    }

    private HashMap<String, Object> parseRoutineCall(String name) throws WrongSyntaxException {
        HashMap<String, Object> temp = new HashMap<>();
        temp.put("statement", "call");
        temp.put("variable", name);
        String word = nextWord();
        if (word.equals("$$lbr")) {
            ArrayList<HashMap<String, Object>> params = new ArrayList<>();
            if(index < tokens.size()){
                if(tokens.get(index).equals("$$rbr")){
                    temp.put("parameters", params);
                    ++index;
                    return temp;
                }
            }
            params.add(parseExpression());
            word = nextWord();
            if (word.equals("$$comm")) {
                while (!word.equals("$$rbr")) {
                    params.add(parseExpression());
                    word = nextWord();
                    if (!word.equals("$$comm")) {
                    }
                }
            }
            temp.put("parameters", params);
        } else {
            --index;
        }

        return temp;
    }

    private HashMap<String, Object> parseWhileLoop() throws WrongSyntaxException {
        HashMap<String, Object> temp = new HashMap<>();
        String word = nextWord();
        temp.put("statement", "while");
        temp.put("expression", parseExpression());
        word = nextWord();
        if (word.equals("$$loop")) {
            temp.put("body", parseBody());
            ++index;
        } else throw new WrongSyntaxException("Unexpected token " + word + ". Expecting 'loop'.");

        return temp;
    }

    //"for" Identifier "in" ["reverse"] Range "loop" Body "end"
    private HashMap<String, Object> parseForLoop() throws WrongSyntaxException {
        HashMap<String, Object> temp = new HashMap<>();
        String word = nextWord();
        temp.put("statement", "for");
        if (parseIdentifier(word)) {
            temp.put("identifier", word.replaceAll("\\$", ""));
            word = nextWord();
            if (word.equals("$$in")) {
                word = nextWord();
                temp.put("reverse", word.equals("$$reverse"));
                if (!word.equals("$$reverse")){
                    --index;
                }
                temp.put("range", parseRange());
                word = nextWord();
                if (word.equals("$$loop")) {
                    temp.put("body", parseBody());
                    ++index;
                } else throw new WrongSyntaxException("Unexpected token " + word + ". Expecting 'loop'.");
            }  else throw new WrongSyntaxException("Unexpected token " + word + ". Expecting 'in'.");
        } else throw new WrongSyntaxException("Incorrect name for identifier. Got " + word);
        return temp;
    }

    //Range : Expression ".." Expression
    private HashMap<String, Object> parseRange() throws WrongSyntaxException {
        HashMap<String, Object> temp = new HashMap<>();
        String word = nextWord();
        temp.put("expression_from", parseExpression());
        word = nextWord();
        if (word.equals("$$doubdot")) {
            temp.put("expression_to", parseExpression());
        } else {
            throw new WrongSyntaxException("Expected '..' range sign, got: " + word);
        }
        return temp;
    }

    //IfStatement : "if" Expression "then" Body ["else" Body] "end"
    private HashMap<String, Object> parseIfStatement() throws WrongSyntaxException {
        HashMap<String, Object> temp = new HashMap<>();
        temp.put("statement", "if");
        String word = nextWord();
        temp.put("expression", parseExpression());
        word = nextWord();
        if (word.equals("$$then")) {
            temp.put("body", parseBody());
            if (tokens.get(index).equals("$$else")) {
                ++index;
                temp.put("else", "true");
                temp.put("else_body", parseBody());
                ++index;
            } else {
                temp.put("else", "false");
                ++index;
            }
        }
        return temp;
    }

    private ArrayList<HashMap<String, Object>> parseParameters() throws WrongSyntaxException {
        ArrayList<HashMap<String, Object>> params = new ArrayList<>();
        String word = nextWord();
        if (!word.equals("$$rbr")) {
            params.add(parseParameterDeclaration(word));
        }else {
            return params;
        }
        word = nextWord();
        if (word.equals("$$comm")) {
            word = nextWord();
            while (!word.equals("$$rbr")) {
                params.add(parseParameterDeclaration(word));
                word = nextWord();
                if (word.equals("$$comm")) {
                    word = nextWord();
                }
            }
        }
        return params;
    }

    private HashMap<String, Object> parseParameterDeclaration(String word) throws WrongSyntaxException {
        HashMap<String, Object> temp = new HashMap<>();
        if (parseIdentifier(word)) {
            temp.put("name", word.replaceAll("\\$", ""));
            String nxt = nextWord();
            if (nxt.equals("$$col")) {
                temp.put("type", parseType());
            }
        } else throw new WrongSyntaxException("Incorret identifier for routine parameter:" + word);
        return temp;
    }

    private ArrayList<HashMap<String, Object>> parseBody() throws WrongSyntaxException {
        ArrayList<HashMap<String, Object>> content = new ArrayList<>();
        String word = nextWord();
        while (!word.equals("$$end") && !word.equals("$$else") && !word.equals("")) {
            if(!word.equals("$$semicol")){
                --index;
                content.add(parseStatement());
            }
            word = nextWord();
        }
        --index;
        return content;
    }

    //Expression : Relation { ("$$and" | "$$or" | "xor") } Relation
    private HashMap<String, Object> parseExpression() throws WrongSyntaxException {
        HashMap<String, Object> temp = new HashMap<>();
        temp.put("left", parseRelation());
        temp.put("is", "expression");
        String word = "";
        if (index < tokens.size()) {
            word = tokens.get(index);
        }
        if (word.equals("$$and") || word.equals("$$or") || word.equals("$$xor")) {
            temp.put("hasright", "true");
            temp.put("op", word.replaceAll("\\$", ""));
            ++index;
            word = nextWord();
            --index;
            temp.put("right", parseRelation());
            HashMap<String, Object> out = new HashMap<>();
            boolean hadMore = false;
            word = nextWord();
            --index;
            while (word.equals("$$and") || word.equals("$$or") || word.equals("$$xor")) {
                hadMore = true;
                ++index;
                out.put("left", parseRelation());
                out.put("right", new HashMap<String, Object>(temp));
                out.put("op", word.replaceAll("\\$", ""));
                temp = out;
                word = nextWord();
            }
            if (hadMore) {
                --index;
            }
        } else {
            temp.put("hasright", "false");
        }

        return temp;
    }


    //Relation : Simple [ ( "$$less" | "$$lesseq" | "$$great" | "$$greateq" | "$$eq" | "noteq" ) Simple ]
    private HashMap<String, Object> parseRelation() throws WrongSyntaxException {
        HashMap<String, Object> temp = new HashMap<>();
        temp.put("left", parseSimple());
        temp.put("is", "relation");
        String word = "";
        if (index < tokens.size()) {
            word = tokens.get(index);
        }
        if (isRelationSign(word)) {
            temp.put("hasright", "true");
            temp.put("op", word.replaceAll("\\$", ""));
            ++index;
            word = nextWord();
            --index;
            temp.put("right", parseSimple());
            HashMap<String, Object> out = new HashMap<>();
            boolean hadMore = false;
            word = nextWord();
            --index;
            while (isRelationSign(word)) {
                hadMore = true;
                ++index;
                out.put("left", parseSimple());
                out.put("right", new HashMap<String, Object>(temp));
                out.put("op", word.replaceAll("\\$", ""));
                temp = out;
                word = nextWord();
            }
            if (hadMore) {
                --index;
            }
        } else {
            temp.put("hasright", "false");
        }

        return temp;
    }

    private boolean isRelationSign(String word) {
        return word.equals("$$less") || word.equals("$$lesseq") || word.equals("$$great") ||
                word.equals("$$greateq") || word.equals("$$eq") || word.equals("$$noteq");
    }

    //Simple : Factor { ( "mul" | "div" | "perc" ) Factor }
    private HashMap<String, Object> parseSimple() throws WrongSyntaxException {
        HashMap<String, Object> temp = new HashMap<>();
        temp.put("left", parseFactor());
        temp.put("is", "simple");
        String word = "";
        if (index < tokens.size()) {
            word = tokens.get(index);
        }
        if (word.equals("$$mul") || word.equals("$$div") || word.equals("$$perc")) {
            temp.put("hasright", "true");
            temp.put("op", word.replaceAll("\\$", ""));
            ++index;
            word = nextWord();
            --index;
            temp.put("right", parseFactor());
            HashMap<String, Object> out = new HashMap<>();
            word = nextWord();
            --index;
            boolean hadMore = false;
            while (word.equals("$$mul") || word.equals("$$div") || word.equals("$$perc")) {
                hadMore = true;
                ++index;
                out.put("left", parseFactor());
                out.put("right", new HashMap<String, Object>(temp));
                out.put("op", word.replaceAll("\\$", ""));
                temp = out;
                word = nextWord();
            }
            if (hadMore) {
                --index;
            }
        } else {
            temp.put("hasright", "false");
        }

        return temp;
    }

    // Factor : Summand { ( "add" | "sub" ) Summand }
    private HashMap<String, Object> parseFactor() throws WrongSyntaxException {
        HashMap<String, Object> temp = new HashMap<>();
        temp.put("left", parseSummand());
        String word = "";
        if (index < tokens.size()) {
            word = tokens.get(index);
        }
        temp.put("is", "factor");
        if (word.equals("$$add") || word.equals("$$sub")) {
            temp.put("hasright", "true");
            temp.put("op", word.replaceAll("\\$", ""));
            ++index;
            word = nextWord();
            --index;
            temp.put("right", parseSummand());
            HashMap<String, Object> out = new HashMap<>();
            boolean hadMore = false;
            word = nextWord();
            --index;
            while (word.equals("$$add") || word.equals("$$sub")) {
                hadMore = true;
                ++index;
                out.put("left", parseSummand());
                out.put("right", new HashMap<String, Object>(temp));
                out.put("op", word.replaceAll("\\$", ""));
                temp = out;
                word = nextWord();
            }
            if (hadMore) {
                --index;
            }
        } else {
            temp.put("hasright", "false");
        }

        return temp;
    }

    //Summand : Primary | "lbr" Expression "rbr"
    private HashMap<String, Object> parseSummand() throws WrongSyntaxException {
        HashMap<String, Object> temp = new HashMap<>();
        String word = "";
        if (index < tokens.size()) {
            word = tokens.get(index);
        }
        temp.put("is", "summand");
        if (word.equals("$$lbr")) {
            nextWord();
            temp.put("left", parseExpression());
            ++index;
        } else {
            temp.put("left", parsePrimary());
        }

        return temp;
    }

    /*
    Primary : [ Sign | "$$not" ] IntegerLiteral
        | [ Sign ] RealLiteral
        | "true"
        | "false"
        | ModifiablePrimary
        | RoutineCall
     */
    private HashMap<String, Object> parsePrimary() throws WrongSyntaxException {
        HashMap<String, Object> temp = new HashMap<>();
        String word = "";
        if (index < tokens.size()) {
            word = tokens.get(index);
        }
        temp.put("is", "primary");
        if (word.equals("$$true") || word.equals("$$false")) {
            temp.put("type", "boolean");
            temp.put("value", word.replaceAll("\\$", ""));
            ++index;
        } else if (word.equals("$$add") || word.equals("$$sub") || word.equals("$$not")
                || parseIntegerLiteral(word) || parseRealLiteral(word)) {

            if (word.equals("$$add") || word.equals("$$sub") || word.equals("$$not")) {
                temp.put("sign", word.equals("$$add") ? "+" : (word.equals("$$sub") ? "-" : "$$not"));
                if (word.equals("$$not")) {
                    word = nextWord();
                    if (parseIntegerLiteral(word)) {
                        temp.put("type", "integer");
                        temp.put("value", word.replaceAll("\\$", ""));
                    }
                } else {
                    ++index;
                    word = nextWord();
                    --index;
                }
            }
            if (!(word.equals("$$add") || word.equals("$$sub"))) {
                if (parseIntegerLiteral(word)) {
                    temp.put("type", "integer");
                    temp.put("value", word.replaceAll("\\$", ""));
                } else if (parseRealLiteral(word)) {
                    temp.put("type", "real");
                    temp.put("value", word.replaceAll("\\$", ""));
                }
                ++index;
            }
        } else if (parseIdentifier(word)) {
            ++index;
            String op = nextWord();
            --index;
            if (op.equals("$$lbr")) {
                temp.put("type", "routinecall");
                temp.put("value", parseRoutineCall(word));
            } else {
                --index;
                temp.put("type", "modifiable");
                temp.put("value", parseModifiablePrimary());
            }
        }

        return temp;
    }

    private boolean parseIntegerLiteral(String word) {
        Pattern p = Pattern.compile("[0-9]*");
        return p.matcher(word).matches();
    }

    private boolean parseRealLiteral(String word) {
        Pattern p = Pattern.compile("[0-9]*\\.[0-9]*");
        return p.matcher(word).matches();
    }

    private boolean parseIdentifier(String word) {
        Pattern p = Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_]*");
        return p.matcher(word).matches() && !(isKeyword(word));
    }

    //ModifiablePrimary: Identifier { "$$dot" Identifier | "lsbr" Expression "rsbr" }
    private HashMap<String, Object> parseModifiablePrimary() throws WrongSyntaxException {
        HashMap<String, Object> temp = new HashMap<>();
        temp.put("statement", "modifiable");
        String word = nextWord();
        if (parseIdentifier(word)) {
            temp.put("value", word.replaceAll("\\$", ""));
            ArrayList<HashMap<String, Object>> chain = new ArrayList<>();
            word = nextWord();
            while (true) {
                if (word.equals("$$dot") || word.equals("$$lsbr")) {
                    HashMap<String, Object> temp2 = new HashMap<>();
                    if (word.equals("$$dot")) {
                        temp2.put("type", "dot");
                        word = nextWord();
                        if(word.equals("")){
                            word = tokens.get(tokens.size()-1);
                            ++index;
                        }
                        if (parseIdentifier(word)) {
                            temp2.put("value", word.replaceAll("\\$", ""));
                        }else throw new WrongSyntaxException("Incorrect identifier. Got: " + word);
                    } else {
                        temp2.put("type", "expression");
                        temp2.put("value", parseExpression());
                        nextWord();
                    }
                    chain.add(temp2);
                    word = nextWord();
                } else {
                    --index;
                    break;
                }

            }
            temp.put("mods", chain);
        }

        return temp;
    }

    private boolean isKeyword(String s) {
        for (int i = 0; i < keywords.length; i++) {
            if (keywords[i].equals(s)) {
                return true;
            }
        }
        return false;
    }

}
