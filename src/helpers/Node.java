package helpers;

import java.util.LinkedHashMap;
import java.util.List;

public abstract class Node {

    private String name;

    Node (String name){
        this.name = name;
    }


    public String getName(){
        return name;
    }


    public abstract String getMethod();

    public abstract LinkedHashMap<String, Symbol> getSymbols() throws Exception;

    //checks if a calculation between two types is possible
    protected boolean checkOperable(String a, String b){

        //I don't know, maybe not (with real and boolean)
        if ((a.equals("real") && b.equals("boolean")) || (a.equals("boolean") && b.equals("real"))){
            return false;
        }
        else if ((a.equals("array") && !(b.equals("array"))) || (!a.equals("array") && (b.equals("array")))){
            return false;
        }
        else if ((a.equals("record") && !(b.equals("record"))) || (!a.equals("record") && (b.equals("record")))){
            return false;
        }
        else return true;
    }

    //gets the result of operation between two types: if it happens that the types are incompatible, throws exception
    protected String getTypesResult(String a, String b) throws Exception{
        if (a.equals("array") && b.equals("array")) return "array";
        else if (a.equals("record") && b.equals("record")) return "record";
        else if (a.equals("integer") && b.equals("integer")) return "integer";
        else if (a.equals("real") && b.equals("real")) return "real";
        else if (a.equals("boolean") && b.equals("boolean")) return "boolean";
        else if ((a.equals("real") && b.equals("integer")) || (a.equals("integer") && b.equals("real"))) return "real";
        else throw new Exception("Incompatible types: "+a+", "+b);
    }


}

