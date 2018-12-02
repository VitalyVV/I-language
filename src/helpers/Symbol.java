package helpers;

//keeps information about the symbol: its name and type
//maybe a value should be added too, if it will be necessary for some semantics checks
public class Symbol {

    Symbol(Object c, String name){
        this.name = name;
        type = c.getClass();
    }

    private String name;

    private Class type;

    public String getName(){
        return name;
    }

    public Class getType(){
        return type;
    }


}
