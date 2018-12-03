import helpers.Node;
import helpers.ProgramNode;
import helpers.RoutineNode;
import helpers.Symbol;

import java.util.*;

public class SemanticAnalyzer{


    private SymbolTable currentScope = null;


    //Contains symbols, which keep insert order
    private class SymbolTable {

        private Map<String, Symbol> symbols = new LinkedHashMap<>();
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

//    private void visit(Node node){
//        if (node!=null){
//        String method = node.getMethod();
//        if (method.equals("Root")) visitRoot(node);
//        else if (method.equals("Routine")) visitRoutine(node);
//        //TODO add other scopes
//    }
//    }

    private void visitRoot(ProgramNode node){
        SymbolTable globalScope = new SymbolTable(node.getName(), 1);
        this.currentScope = globalScope;


        //get all declared variables
        try{
            LinkedHashMap<String, Symbol> symbols = node.getSymbols();
            if (symbols!=null) globalScope.insert(symbols);
        }catch (Exception e){
            e.printStackTrace();
            return;
        }

        globalScope.printTable();
        //TODO - check declared values

        //go through every declared routine symbol table
        while(true){
            RoutineNode rnode = node.getChild();
            if (rnode == null) break;
            globalScope.insert(new Symbol("routine", rnode.getName(), rnode.getRoutine()));
            visitRoutine(rnode);
        }
    }

    private void visitRoutine (RoutineNode node){
        SymbolTable procScope = new SymbolTable(node.getName(), this.currentScope.level+1, this.currentScope);
        currentScope = procScope;
        System.out.println(procScope.name+" "+procScope.level);
        //fetching parameters - TODO

//        visit(node.getChild());
        this.currentScope = this.currentScope.enclosingScope;

    }


    public void analyze(ArrayList<HashMap<String, Object>> toAst){
        ProgramNode program = new ProgramNode("Program", toAst);
        visitRoot(program);
    }

//    public void check(){
//        Node start = new ProgramNode("proga");
//        visit(start);
//    }


}


