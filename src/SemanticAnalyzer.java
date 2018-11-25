import java.util.LinkedHashMap;
import java.util.Map;

public class SemanticAnalyzer {


    //keeps information about the symbol: its name and type
    //maybe a value should be added too, if it will be necessary for some semantics checks
    private class Symbol {

        Symbol(Object c, String name){
            this.name = name;
            type = c.getClass();
        }

        private String name;

        private Class type;

        String getName(){
            return name;
        }

        Class getType(){
            return type;
        }


    }

    //Contains symbols, which keep insert order
    private class SymbolTable {

        private Map<String, Symbol> symbols = new LinkedHashMap<>();


        //add new symbol
        void insert(Symbol symbol){
            symbols.put(symbol.getName(), symbol);
        }

        //get a Symbol by its name
        protected Symbol lookup(String name){
            return symbols.get(name);
        }

        //just ofr check
        void printTable(){
            for (String key: symbols.keySet()){
                System.out.println(key+" "+symbols.get(key).getType().toString());
            }
        }
    }

    //just for check
    public void initSymbols(){

        Symbol x = new Symbol(15, "a");
        Symbol y = new Symbol (36.6, "b");

        SymbolTable table = new SymbolTable();
        table.insert(x);
        table.insert(y);

        table.printTable();


    }

}


