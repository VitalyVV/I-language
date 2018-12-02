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

    private void visit(Node node){
        if (node!=null){
        String method = node.getMethod();
        if (method.equals("Root")) visitRoot(node);
        else if (method.equals("Routine")) visitRoutine(node);
        //TODO add other scopes
    }
    }

    private void visitRoot(Node node){
        SymbolTable globalScope = new SymbolTable(node.getName(), 1);
        this.currentScope = globalScope;

        List<Symbol> symbols = node.getSymbols();
        if (!(symbols == null)){
            for (Symbol s: symbols){
                globalScope.insert(s);

            }
        }

        globalScope.printTable();
        //TODO - check declared values

        visit(node.getChild());
    }

    private void visitRoutine (Node node){
        SymbolTable procScope = new SymbolTable(node.getName(), this.currentScope.level+1, this.currentScope);
        currentScope = procScope;
        System.out.println(procScope.name+" "+procScope.level);
        //fetching parameters - TODO

        visit(node.getChild());
        this.currentScope = this.currentScope.enclosingScope;

    }


    public void analyze(ArrayList<HashMap<String, Object>> toAst){
        ProgramNode program = new ProgramNode("Program", toAst);
        visit(program);

    }

//    public void check(){
//        Node start = new ProgramNode("proga");
//        visit(start);
//    }


}


