import helpers.*;

import java.util.*;

public class SemanticAnalyzer{


    private SymbolTable currentScope = null;


    //Contains symbols, which keep insert order
    private class SymbolTable {

        private LinkedHashMap<String, Symbol> symbols = new LinkedHashMap<>();
        private HashMap<String, String> types = new HashMap<>();
        private String name;
        private int level;
        private SymbolTable enclosingScope = null;

        SymbolTable(String name, int level){
            this.name = name;
            this.level = level;
        }

        SymbolTable(String name, int level, SymbolTable enclosingScope){
            this.name = name;
            this.level = level;
            this.enclosingScope = enclosingScope;
        }


        //add new symbol
        void insert(Symbol symbol){
            symbols.put(symbol.getName(), symbol);
        }

        void insert(LinkedHashMap<String, Symbol> symbols){
            this.symbols = symbols;
        }

        void insertTypes(HashMap<String, String> types){
            this.types = types;
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

    public void analyze(ArrayList<HashMap<String, Object>> toAst) throws Exception{
        ProgramNode program = new ProgramNode("Program", toAst);
        program.createSymbols();
    }

//    public void check(){
//        Node start = new ProgramNode("proga");
//        visit(start);
//    }


}


