package helpers;

import java.util.List;

public abstract class Node {

    private String name;

    Node (String name){
        this.name = name;
    }


    public String getName(){
        return name;
    }

    public abstract Node getChild();

    public abstract String getMethod();

    public abstract List<Symbol> getSymbols();


}

