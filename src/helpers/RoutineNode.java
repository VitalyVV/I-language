package helpers;

import com.sun.corba.se.impl.io.TypeMismatchException;
import jdk.nashorn.internal.runtime.regexp.joni.exception.SyntaxException;

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

    public RoutineNode(String name, HashMap<String, Object> routine){
        super(name);
        this.routine = routine;
    }

    public void setSymbols(LinkedHashMap<String, Symbol> symbols){
        this.symbolsDeclarations = (LinkedHashMap<String, Symbol>) symbols.clone();
    }

    public void setTypes(HashMap<String, String> types){

        this.typeMappings =(HashMap<String, String>) types.clone();
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
            return null;
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
        if (routine.keySet().contains("parameters")){
            ArrayList<HashMap<String, Object>> params = (ArrayList<HashMap<String, Object>>) routine.get("parameters");
            numParams = params.size();
            //add parameters to param list and symbols
            for (HashMap<String, Object> elem:params){
                if (elem.keySet().contains("name")) {
                    String parName = (String) elem.get("name");

                    String type = getType(elem.get("type"));
                    Symbol s = new Symbol(type, parName, elem);

                    if (symbolsDeclarations.keySet().contains(parName)){
                        symbolsDeclarations.remove(parName);

                    }
                    symbolsDeclarations.put(parName, s);
//                    innerSymbolsDeclarations.put(parName, s);
                    parameters.add(s);
                }}

            }

            //parse body statements if exist
        if (routine.keySet().contains("hasbody")){

            parseRoutineBody(routine.get("body"));
        }

        entries = innerSymbolsDeclarations.entrySet().iterator();
    }

    private void parseRoutineBody(Object bodyR) throws Exception{
        ArrayList<HashMap<String, Object>> body = (ArrayList<HashMap<String, Object>>)bodyR;
        for (HashMap<String, Object> elem: body){
            if (elem.keySet().contains("Content")){
                HashMap<String, Object> cont = (HashMap<String, Object>) elem.get("Content");

                //check function call
                if (((String)cont.get("statement")).equals("call")){
                    String name = (String) cont.get("variable");
                    if (symbolsDeclarations.keySet().contains(name) && symbolsDeclarations.get(name).getType().equals("routine")){
                        Symbol routineS = symbolsDeclarations.get(name);
                        RoutineNode rnode = (RoutineNode) routineS.getUnit();
                        if (cont.keySet().contains("parameters")){

                            //check that the function we call already exists, and number ant types of parameters we pass match
                            ArrayList<HashMap<String, Object>> params = (ArrayList<HashMap<String, Object>>) cont.get("parameters");
                            if (!(rnode.getNumParams() == params.size())){
                                throw new Exception("Amount of passed parameters does not match." +
                                        "Expected: "+rnode.getNumParams()+" , got: "+params.size());
                            }
                            ArrayList <Symbol> existedSymbols = rnode.getParameters();
                            int parCounter = 0;
                            for (HashMap<String, Object> el:params){
                                   String type = getType(el);
                                   String actType = existedSymbols.get(parCounter).getType();
                                   if (!type.equals(actType))
                                       throw new TypeMismatchException("Parameters types passed to routine does not match." +
                                               type+" != "+actType);
                            }

                        }
                    }
                    else
                        throw new Exception("No such identifier previously declared: "+name);
                }

                //check assignment correctness
                else if (((String)cont.get("statement")).equals("assignment")){

                    String toAssign = calculateExpressionResult(cont.get("value"));
                    String assignable;
                    HashMap<String, Object> modiPrim = (HashMap<String, Object>) cont.get("name");
                    String modPrimName = (String)modiPrim.get("value");

                    //check if have already declared the variable to which we assign
                    if (!(symbolsDeclarations.keySet().contains(modPrimName) || innerSymbolsDeclarations.keySet().contains(modPrimName))) {
                        throw new Exception("No such identifier previously declared: "+modPrimName);
                    }
                    assignable = getModifiableType(modiPrim);

                    //check for assignment result according to obtained types, if no match - exception is thrown
                    if (assignable == null){
                         if (innerSymbolsDeclarations.keySet().contains(modPrimName)){
                            assignable = getType(innerSymbolsDeclarations.get(modPrimName).getUnit());
                         Symbol s = innerSymbolsDeclarations.get(modPrimName);
                         s.setType(getAssignmentresult(assignable,toAssign ));}
                         else {
                             assignable = getType(symbolsDeclarations.get(modPrimName).getUnit());
                             Symbol s = symbolsDeclarations.get(modPrimName);
                             s.setType(getAssignmentresult(assignable,toAssign ));

                         }
                    }
                    else {
                        if (innerSymbolsDeclarations.keySet().contains(modPrimName)){
                            Symbol s = innerSymbolsDeclarations.get(modPrimName);
                            s.setType(getAssignmentresult(assignable,toAssign ));}
                        else {
                            Symbol s = symbolsDeclarations.get(modPrimName);
                            s.setType(getAssignmentresult(assignable,toAssign ));

                        }
                    }

                }

                //create new sub-scope if
                else if (((String)cont.get("statement")).equals("if")){
                    innerSymbolsDeclarations.put("if"+ifcounter, new Symbol("if", "if"+ifcounter, cont));
                    ifcounter++;
                }

                //create new subscope for
                else if (((String)cont.get("statement")).equals("for")){
                    innerSymbolsDeclarations.put("for"+forcounter, new Symbol("for", "for"+forcounter, cont));
                    forcounter++;
                }

                //create new subscope while
                else if (((String)cont.get("statement")).equals("while")){
                    innerSymbolsDeclarations.put("while"+whilecounter, new Symbol("while", "while"+whilecounter, cont));
                    whilecounter++;

                }

                //check for variable declaration
                else if (((String)cont.get("statement")).equals("var")){
                    if (((String) cont.get("hastype")).equals("true")) {
                        //if type is already declared via "var a: integer"
                        Symbol s = null;
                        String varType = getType(cont.get("type"));
                        if (varType.equals("record") || varType.equals("array"))
                        {
                            s = new Symbol(varType, (String) cont.get("name"), cont);
                        } else if (cont.containsKey("value")) { //Vars that have type may have "is" expressions too
                            //Inference expression type
                            String expressionResult = calculateExpressionResult(cont.get("value"));
                            //If expression type is the same as declared type
                            if (expressionResult.equals(varType))
                            {
                                s = new Symbol(varType, (String) cont.get("name"),cont);
                            } else throw new TypeMismatchException("\nWrong expression result type for: " + cont.get("name") +
                                    ".\nExpected: "+ varType+". Got: " + expressionResult);

                        } else s = new Symbol(varType, (String) cont.get("name"), cont);

                        symbolsDeclarations.put((String) cont.get("name"), s);
                        innerSymbolsDeclarations.put((String) cont.get("name"), s);
                    } else {
                        //if type is declared via expression calculation like "var a: integer is (5+5)*10"
                        String valueType = calculateExpressionResult(cont.get("expression"));
                        Symbol s = new Symbol(valueType, (String) cont.get("name"), cont);
                        symbolsDeclarations.put((String) cont.get("name"), s);
                        innerSymbolsDeclarations.put((String) cont.get("name"), s);
                    }
                }
                else if (((String)cont.get("statement")).equals("type")){
                    //Add type mapping to the list
                    if (typeMappings.keySet().contains(cont.get("name"))){
                        typeMappings.remove(cont.get("name"));
                        typeMappings.put((String) cont.get("name"), getType(cont.get("type")));
                    }
                }
            }
        }

    }


    private String getModifiableType(Object o) throws Exception{

        HashMap<String, Object> modiPrim = (HashMap<String, Object>) o;
        String modPrimName = (String)modiPrim.get("value");
        ArrayList<HashMap<String, Object>> modifvar = (ArrayList<HashMap<String, Object>>) modiPrim.get("mods");
        if (modifvar.isEmpty()) {
            return null;
        }
        else {
            //Get member access list
            String type = "";
            ArrayList<HashMap<String, Object>> members = null;
            //Check if modifiable exists
            if (symbolsDeclarations.containsKey(modPrimName))
            {
                if (modifvar.get(0).get("type").equals("dot"))
                {
                    //Get modifiable members
                    members = getMembers((HashMap<String, Object>) symbolsDeclarations.get(modPrimName).getUnit());
                    //Check if access is possible and get type of the member
                    type = getRecordType(members, modifvar);
                } else if (modifvar.get(0).get("type").equals("expression"))
                    //Check if access is possible and get type of the member
                    type = getArrayType(modiPrim, modifvar);
                return type;
            }
            else throw new SyntaxException("Undeclared record: " + modPrimName);

        }

    }

    public Map.Entry<String, Symbol> getChild(){

        if (currentChild>=innerSymbolsDeclarations.size()) return null;
        Map.Entry<String,Symbol> entry = entries.next();
        currentChild++;
        //update the pointer for routine we have not visited before
        return entry;
    }
}
