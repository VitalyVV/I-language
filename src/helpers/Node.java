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


}

