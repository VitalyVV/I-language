package helpers;

import com.sun.corba.se.impl.io.TypeMismatchException;

import javax.naming.OperationNotSupportedException;
import java.util.*;

public abstract class Node {

    private String name;
    private int currentChild = 0;

    //list of variable symbols only
    protected LinkedHashMap<String, Symbol> symbolsDeclarations = new LinkedHashMap<>();
    //type declarations
    protected HashMap<String,String> typeMappings = new HashMap<>();

    Iterator<Map.Entry<String,Symbol>>entries = symbolsDeclarations.entrySet().iterator();
    //list of routines only
    protected LinkedList<RoutineNode> routines = new LinkedList<>();
    protected LinkedList<String> namesRoutines = new LinkedList<>();

    Node (String name){
        this.name = name;
    }


    public String getName(){
        return name;
    }


    public abstract String getMethod();

    public abstract LinkedHashMap<String, Symbol> getSymbols() throws Exception;

    //checks if a calculation between two types is possible
    protected boolean checkOperable(String a, String b){

        //I don't know, maybe not (with real and boolean)
        if ((a.equals("real") && b.equals("boolean")) || (a.equals("boolean") && b.equals("real"))){
            return false;
        }
        else return true;
    }

    //gets the result of operation between two types: if it happens that the types are incompatible, throws exception
    protected String getTypesResult(String a, String b, String op) throws Exception{
        if (a.equals("integer") && b.equals("integer") && !op.equals("factor")) return "integer";
        else if (a.equals("integer") && b.equals("integer") && op.equals("factor")) return "real";
        else if (a.equals("real") && b.equals("real")) return "real";
        else if (a.equals("boolean") && b.equals("boolean")) return "boolean";
        else if ((a.equals("real") && b.equals("integer")) || (a.equals("integer") && b.equals("real"))) return "real";
        else throw new Exception("Incompatible types: "+a+", "+b+ " with operand "+op);
    }

    //get type for variable using the one declared in specified hashmap (also perform a check for a variable to
    // have been  already declared)
    protected String getType(Object unit) throws Exception{
        HashMap<String, Object> a= (HashMap<String, Object>) unit;
        if (a.containsKey("primitive")){
            return (String)a.get("primitive");
        }
        else if (a.containsKey("identifier")){
            String typeVarName = (String)a.get("identifier");
            if (symbolsDeclarations.keySet().contains(typeVarName)){
                return symbolsDeclarations.get(typeVarName).getType();
            } else if (typeMappings.containsKey(typeVarName))
            {
                //Get mapped basic type from the list
                return typeMappings.get(typeVarName);
            }
            else throw new Exception("No such identifier declared: "+typeVarName);
        }
        else if (a.containsKey("array")){
            return "array";
        }
        else if (a.containsKey("record")){
            return "record";
        }
        else {
            Map.Entry<String,Object> entry = a.entrySet().iterator().next();
            throw  new Exception("No such type exists: "+entry.getKey());
        }
    }

    public String getOpType(String op) throws OperationNotSupportedException {
        List<String> factors = Arrays.asList("div","mul","perc");
        List<String> summands = Arrays.asList("sub","add");
        List<String> relations = Arrays.asList("and", "or", "xor");
        List<String> simple = Arrays.asList("less", "lesseq", "great","greateq", "eq", "noteq");

        if (factors.contains(op))return "factor";
        else if (summands.contains(op))return "summand";
        else if (relations.contains(op))return "relation";
        else if (simple.contains(op)) return  "simple";
        else throw new OperationNotSupportedException("Invalid operand: "+ op);
    }


    //just go down the tree and get the type of expression - TODO
    protected String calculateExpressionResult(Object o) throws Exception{

        HashMap<String, Object> hashmaped = (HashMap<String, Object>) o;

        if (((String)hashmaped.get("is")).equals("primary")){
            String typePrimary = ((String)hashmaped.get("type"));
            if (typePrimary.equals("boolean") || typePrimary.equals("integer")||typePrimary.equals("real")){
                return typePrimary;
            }
            else if (typePrimary.equals("modifiable")){
                HashMap<String, Object> modifvar = (HashMap<String, Object>) hashmaped.get("value");
                String modifName = (String) modifvar.get("value");
                if (((ArrayList<HashMap<String, Object>>)modifvar.get("mods")).isEmpty()) {
                    if (symbolsDeclarations.keySet().contains(modifName)){
                        return symbolsDeclarations.get(modifName).getType();
                    }
                    else if (namesRoutines.contains(modifName)){
                        int ind = namesRoutines.indexOf(modifName);
                        return routines.get(ind).getResultType();
                    }
                    else throw new Exception("No such identifier declared: "+modifName);
                }
                else{
                    ArrayList<HashMap<String, Object>> mods = (ArrayList<HashMap<String, Object>>)modifvar.get("mods");
                    String submode = modifName;
                    String type = "";

                    //TODO - maybe add list for usertypes
                    if (symbolsDeclarations.keySet().contains(modifName)){
//                        ArrayList<HashMap<String, Object>> mods1 = (ArrayList<HashMap<String, Object>>)symbolsDeclarations.get(modifName).;
//                        for (HashMap<String, Object> elem:mods){
//                            if (((String) elem.get("type")).equals("dot")){
//                                if (symbolsDeclarations.get(modifName)){
//                                    return symbolsDeclarations.get(modifName).getType();
//                                }
//                                else if (namesRoutines.contains(modifName)){
//                                    int ind = namesRoutines.indexOf(modifName);
//                                    return routines.get(ind).getResultType();
//                                }
//                                else throw new Exception("No such identifier declared: "+modifName);
//                            }
//                        }
                    }
                }
            }
            else if (typePrimary.equals("routinecall")){
                HashMap<String, Object> hashmapedRotineCall = (HashMap<String, Object>) hashmaped.get("type");
                String name = (String)hashmapedRotineCall.get("name");
                if (namesRoutines.contains(name)){
                    int index = namesRoutines.indexOf(name);
                    RoutineNode rnode = routines.get(index);
                    return rnode.getResultType();
                }
            }
            else throw new Exception("No such type exists: "+typePrimary);
        }

        String result = calculateExpressionResult(hashmaped.get("left"));
        String mapedResultType  = (String)hashmaped.get("is");
        //Logic for factor relation and simple type is identical since all require same type left and right operands
        if (mapedResultType.equals("factor") || mapedResultType.equals("relation") || mapedResultType.equals("simple")){
            if (((String)hashmaped.get("hasright")).equals("true")){
                String resultRight = calculateExpressionResult(hashmaped.get("right"));
                if (checkOperable(result, resultRight)){
                    return getTypesResult(result, resultRight, getOpType((String)hashmaped.get("op")));
                }
            } else return result;
            //Summand and expression have only left operands
        } else if (mapedResultType.equals("summand")) return result;
        else if (mapedResultType.equals("expression")) return result;
        else throw new TypeMismatchException("Syntax analysis failed. Expression type not valid: "+ (String)hashmaped.get("op"));
        return null;
    }

    public Map.Entry<String, Symbol> getChild(){

        if (currentChild>symbolsDeclarations.size()-1) return null;
        Map.Entry<String,Symbol> entry = entries.next();
        //update the pointer for routine we have not visited before
        return entry;
    }

}

