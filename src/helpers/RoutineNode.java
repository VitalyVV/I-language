package helpers;

import com.sun.corba.se.impl.io.TypeMismatchException;

import java.util.*;

public class RoutineNode extends Node {

    private HashMap<String, Object> routine;

    //private LinkedHashMap<String, Symbol> innerSymbolsDeclarations = new LinkedHashMap<>();

    public RoutineNode(String name, HashMap<String, Object> routine){
        super(name);
        this.routine = routine;
    }

    public void setSymbols(LinkedHashMap<String, Symbol> symbols){
        this.symbolsDeclarations = (LinkedHashMap<String, Symbol>) symbols.clone();
    }

    public void setTypes(HashMap<String, String> types){
        this.typeMappings = types;
    }


    public String getResultType(){
        if (((String)routine.get("hastype")).equals("true")){
            try {
                return getType(routine.get("type"));
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        else {
            return null;
        }
    }

    public String getMethod(){
        return "Routine";
    }
    LinkedHashMap<String, Symbol> symbols = new LinkedHashMap<>();

    public LinkedHashMap<String, Symbol> getSymbols(){
        return null;
    }

    public HashMap<String, Object> getRoutine(){
        return routine;
    }


    public void createTable() throws Exception{
        if (routine.keySet().contains("parameters")){
            ArrayList<HashMap<String, Object>> params = (ArrayList<HashMap<String, Object>>) routine.get("parameters");
            for (HashMap<String, Object> elem:params){
                if (elem.keySet().contains("name")){
                    String parName = (String)elem.get("name");
                    if (symbolsDeclarations.keySet().contains(parName)){
                        symbolsDeclarations.remove("name");

                        String type = getType(elem.get("type"));

                        symbolsDeclarations.put(parName, new Symbol(type, parName, elem));

                    }
                }
            }


        }
        if (routine.keySet().contains("hasbody")){
            parseRoutineBody(routine.get("body"));
        }
    }

    private void parseRoutineBody(Object bodyR){
        ArrayList<HashMap<String, Object>> body = (ArrayList<HashMap<String, Object>>)bodyR;
        for (HashMap<String, Object> elem: body){

        }

    }

    private void parseIf(Object ifStatem) throws Exception{
        HashMap<String, Object> temp = (HashMap<String, Object>) ifStatem;
        String typeCond = calculateExpressionResult((HashMap<String, Object>)temp.get("expression"));
        if (!typeCond.equals("boolean")) throw
                new TypeMismatchException("Syntax analysis failed. IF condition not valid: expected - boolean, got: "+typeCond);

        if (temp.keySet().contains("body")){
            parseRoutineBody(temp.get("body"));
        }
    }
}
