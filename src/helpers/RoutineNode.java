package helpers;

import Syntax.WrongSyntaxException;

import java.util.*;

public class RoutineNode extends Node {

    //to name the if, while, for statements
    private int ifcounter = 0;
    private int forcounter = 0;
    private int whilecounter = 0;

    private HashMap<String, Object> routine;

    //number of parameters of function
    private int numParams;

    public int getNumParams(){
        return numParams;
    }

    //only for symbols declared inside the scope
    private LinkedHashMap<String, Symbol> innerSymbolsDeclarations = new LinkedHashMap<>();

    private ArrayList<Symbol> parameters = new ArrayList<>();

    public ArrayList<Symbol> getParameters(){
        return parameters;
    }

    public RoutineNode(String name, HashMap<String, Object> routine) throws Exception {
        super(name);
        this.routine = routine;
        if (routine.containsKey("parameters")){
            ArrayList<HashMap<String, Object>> params = (ArrayList<HashMap<String, Object>>) routine.get("parameters");
            numParams = params.size();
            //add parameters to param list and symbols
            for (HashMap<String, Object> elem:params){
                String parName = (String) elem.get("name");

                String type = getType(elem.get("type"));
                Symbol s = new Symbol(type, parName, elem);
                if (symbolsDeclarations.containsKey(parName)) throw new WrongSyntaxException("Vague syntax. Variable " + parName + " already exists");
                symbolsDeclarations.put(parName, s);
                parameters.add(s);
            }

        }
    }

    //we need symbols from parent scope as they are visible to the child if declared
    public void setSymbols(LinkedHashMap<String, Symbol> symbols){
        this.symbolsDeclarations = (LinkedHashMap<String, Symbol>) symbols.clone();
    }

    public void setTypes(HashMap<String, String> types){

        if (types!=null)
        this.typeMappings = (HashMap<String, String>) types.clone();
    }

    //Checks if given arguments are compatible with the function
    public void checkCompatibility(ArrayList<HashMap<String,Object>> params) throws Exception {
        if (parameters.size()!=params.size()) throw new WrongSyntaxException("Routine call paramter amount is invalid");
        for (int i = 0; i<parameters.size();i++)
        {
            String paramType = calculateExpressionResult(params.get(i));
            if (!parameters.get(i).getType().equals(paramType))
                throw new WrongSyntaxException("Invalid argument type of parameter "
                        + parameters.get(i).getName()+ " .Expected: "+parameters.get(i).getType()+" .Got: "+ paramType);
        }
    }

    //returns smth, if routine has return type
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
            return "Null"; //We need routine types for expression result calculation
        }
    }

    public String getMethod(){
        return "routine";
    }
    LinkedHashMap<String, Symbol> symbols = new LinkedHashMap<>();

    public LinkedHashMap<String, Symbol> getSymbols(){
        return null;
    }

    public HashMap<String, Object> getRoutine(){
        return routine;
    }


    //go through routine declaration and build inner structure
    public void createTable() throws Exception{
        //Allow recursive calls but don't create tables for routine inside herself or something bad is going to happen
        RoutineNode rout = new RoutineNode((String) routine.get("name"), routine);
        //Routines can't read declarations ahead of them
        rout.setSymbols(symbolsDeclarations);
        rout.setTypes(typeMappings);
        //we add it separate list of routines
        routines.add(rout);
        //we add name of routine to find previously declared ones + to check if we actually have declared it
        namesRoutines.add((String) routine.get("name"));
        symbolsDeclarations.put((String) routine.get("name"),
                new Symbol("routine", (String) routine.get("name"), rout));

            //parse body statements if exist
        if (routine.containsKey("body")){
            parseBody((ArrayList<HashMap<String, Object>>) routine.get("body"));
        } else if (routine.get("hastype").equals("true") && !routine.containsKey("body"))  //If there is a return type and no body
            throw new WrongSyntaxException("Routine " + routine.get("name") + " has to return "+ routine.get("type"));
    }


    public Map.Entry<String, Symbol> getChild(){

        if (currentChild>=innerSymbolsDeclarations.size()) return null;
        Map.Entry<String,Symbol> entry = entries.next();
        currentChild++;
        //update the pointer for routine we have not visited before
        return entry;
    }
}
