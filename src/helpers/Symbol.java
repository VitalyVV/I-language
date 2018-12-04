package helpers;

import java.util.HashMap;

//keeps information about the symbol: its name and type
//maybe a value should be added too, if it will be necessary for some semantics checks
public class Symbol {
//public Symbol(String type, String name, HashMap<String, Object> unit){
    public Symbol(String type, String name, Object unit){
        this.name = name;
        this.type = type;
        this.unit = unit;
    }

    private String name;

    private String type;
    private Object unit;

    public String getName(){
        return name;
    }

    public String getType(){
        return type;
    }

    public Object getUnit(){
        return unit;
    }


}
