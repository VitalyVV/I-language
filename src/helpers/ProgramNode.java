package helpers;

import Syntax.WrongSyntaxException;
import com.sun.corba.se.impl.io.TypeMismatchException;
import jdk.nashorn.internal.runtime.regexp.joni.exception.SyntaxException;
import org.omg.IOP.CodecPackage.TypeMismatch;

import javax.naming.OperationNotSupportedException;
import java.util.*;

//I think that functions for processing

public class ProgramNode extends Node {

    public ProgramNode(String name){
        super(name);
    }

    private ArrayList<HashMap<String, Object>> children;

    //list of variable symbols only
    private LinkedHashMap<String, Symbol> symbolsDeclarations = new LinkedHashMap<>();


    //list of routines only
    private LinkedList<RoutineNode> routines = new LinkedList<>();
    private LinkedList<String> namesRoutines = new LinkedList<>();

    //type declarations
    HashMap<String,String> typeMappings = new HashMap<>();

    private int currentChild = 0;


    public String getMethod(){
        return "Root";
    }

    public ProgramNode(String name, ArrayList<HashMap<String, Object>> children){
        super(name);
        this.children = children;

    }

    /*

    public RoutineNode getChild(){

        if (currentChild>routines.size()-1) return null;
        RoutineNode rnode =  routines.get(currentChild);
        currentChild++; //update the pointer for routine we have not visited before
        return rnode;
    }
*/

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

    //Start method to go down the tree and get the types of objects
    public LinkedHashMap<String, Symbol> getSymbols() throws Exception{
        for (HashMap<String, Object> elem: children){

            HashMap<String, Object> rootUnit = ((HashMap<String, Object>) elem.get("Content"));
            String unit = (String) rootUnit.get("statement");
            if (unit.equals("var")) {//variable declaration type extraction
                if (((String) rootUnit.get("hastype")).equals("true")) {
                    //if type is already declared via "var a: integer"
                    Symbol s = null;
                    String varType = getType(rootUnit.get("type"));
                    if (varType.equals("record") || varType.equals("array"))
                    {
                        s = new Symbol(varType, (String) rootUnit.get("name"), rootUnit);
                    } else if (rootUnit.containsKey("value")) { //Vars that have type may have "is" expressions too
                        //Inference expression type
                        String expressionResult = calculateExpressionResult(rootUnit.get("value"));
                        //If expression type is the same as declared type
                        if (expressionResult.equals(varType))
                        {
                            s = new Symbol(varType, (String) rootUnit.get("name"), rootUnit);
                        } else throw new TypeMismatchException("\nWrong expression result type for: " + rootUnit.get("name") +
                                ".\nExpected: "+ varType+". Got: " + expressionResult);

                    } else s = new Symbol(varType, (String) rootUnit.get("name"), rootUnit);

                    symbolsDeclarations.put((String) rootUnit.get("name"), s);
                } else {
                    //if type is declared via expression calculation like "var a: integer is (5+5)*10"
                    String valueType = calculateExpressionResult(rootUnit.get("expression"));
                    Symbol s = new Symbol(valueType, (String) rootUnit.get("name"), rootUnit);
                    symbolsDeclarations.put((String) rootUnit.get("name"), s);
                }
            } else if(unit.equals("declaration")){//if we have routine declaration "routine a() : integer"
                RoutineNode routine = new RoutineNode((String) rootUnit.get("name"), rootUnit);
                //we add it separate list of routines
                routines.add(routine);
                //we add name of routine to find previously declared ones + to check if we actually have declared it
                namesRoutines.add((String) rootUnit.get("name"));
            }else if(unit.equals("type")){//if we have type declaration
                //Add type mapping to the list
                typeMappings.put((String)rootUnit.get("name"),getType(rootUnit.get("type")));
            } else throw new Exception("Invalid syntax. Routine or declaration expected");
        }



//        int a = 5;
//        int b = 6;
//        Symbol s1 = new Symbol(a, "a");
//        Symbol s2 = new Symbol(b, "b");
//
//        List<Symbol> s = new LinkedList<Symbol>();
//        s.add(s1);
//        s.add(s2);
//        return s;
        return symbolsDeclarations;
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

    //Returns members of record hashmap
    public ArrayList<HashMap<String,Object>> getMembers(HashMap<String,Object> unit)
    {
        HashMap<String,Object> type = (HashMap<String,Object>) unit.get("type");
        if (type.containsKey("record"))
        {
            HashMap<String,Object> record = (HashMap<String,Object>) type.get("record");
            ArrayList<HashMap<String,Object>> members = (ArrayList<HashMap<String,Object>>) record.get("content");
            return members;
        } else throw new SyntaxException("Not a record: "+ unit.get("name"));
    }



    private String getRecordType(ArrayList<HashMap<String,Object>> recordMembers, ArrayList<HashMap<String,Object>> accessMembers) throws Exception {
        String currentMod = (String) accessMembers.get(0).get("value");
        //For each available access member
        for (HashMap<String,Object> member:recordMembers)
        {
            //If names match
            if (member.get("name").equals(currentMod))
            {
                //Get next element info
                HashMap<String, Object> type = (HashMap<String, Object>) member.get("type");
                if (type.containsKey("record"))
                {
                    if (accessMembers.size()<=1)
                        return "record";
                    //Peel off one access members and make a recursive call on member's members
                    ArrayList<HashMap<String,Object>> subMembers = new ArrayList<>(accessMembers.subList(1, accessMembers.size()));
                    return getRecordType(getMembers(member), subMembers);
                } else if (type.containsKey("primitive")) {
                    //Check if there are no extra access members
                    if (accessMembers.size()>1) throw new SyntaxException("Modifiable does not have member: " + accessMembers.get(1).get("value"));
                    //If member is primitive
                    return (String) type.get("primitive");
                } else if (type.containsKey("array"))
                {
                    //Array may contain other arrays as well as records and primitives
                    //Get array data
                    HashMap<String,Object> element = (HashMap<String, Object>) type.get("array");
                    //Get next element info
                    type = (HashMap<String, Object>) element.get("type");

                    if (type.containsKey("primitive"))
                    {
                        if (accessMembers.size()>1 && !accessMembers.get(1).get("type").equals("expression")) throw new SyntaxException("Modifiable does not have member: " + accessMembers.get(1).get("value"));

                        //Check index validity
                        HashMap<String, Object> index = accessMembers.get(1);
                        String expressionResult = calculateExpressionResult(index.get("value"));
                        if (!expressionResult.equals("integer"))throw new TypeMismatch("Array element access expression is not integer");

                        //If member is primitive
                        return (String) type.get("primitive");
                    }
                    else if (type.containsKey("record")) //Array of records
                    {
                        //Check index validity
                        HashMap<String, Object> index = accessMembers.get(1);
                        String expressionResult = calculateExpressionResult(index.get("value"));
                        if (!expressionResult.equals("integer"))throw new TypeMismatch("Array element access expression is not integer");

                        //We got only one expression access member == array
                        if (accessMembers.size()<=1)
                            return "array";
                        //We have two access members one is an expression and the other is a .identifier == record
                        if (accessMembers.size()==2)
                            return "record";

                        //If second access member is invalid != dot
                        if (!accessMembers.get(2).get("type").equals("dot")) throw new WrongSyntaxException("Can't access record as an array");
                        //Peel off one access members and make a recursive call on member's members (skip member access)
                        ArrayList<HashMap<String,Object>> subMembers = new ArrayList<>(accessMembers.subList(2, accessMembers.size()));
                        return getRecordType(getMembers(element), subMembers);
                    } else if (type.containsKey("array")) //Array of arrays
                    {
                        if (accessMembers.size()<=2)
                            return "array";
                        if (!accessMembers.get(2).get("type").equals("expression")) throw new WrongSyntaxException("Can't access array as record");
                        //Peel off one access members and make a recursive call on member's members (skip member access)
                        ArrayList<HashMap<String,Object>> subMembers = new ArrayList<>(accessMembers.subList(1, accessMembers.size()));
                        return getArrayType(element, subMembers);
                    }
                }
            } else continue;
        }
        throw new SyntaxException("No such member: "+ currentMod);
    }

    //Type inference exclusive for array of arrays
    private String getArrayType(HashMap<String,Object> array, ArrayList<HashMap<String,Object>> accessMembers) throws Exception {
        HashMap<String, Object> index1 = accessMembers.get(0);
        HashMap<String, Object> index2 = accessMembers.get(1);

        String expressionResult1 = calculateExpressionResult(index1.get("value"));
        String expressionResult2 = calculateExpressionResult(index2.get("value"));

        //Evaluate indices. They should be integers
        if (expressionResult1.equals("integer") && expressionResult2.equals("integer"))
        {
            //Look two elements ahead
             HashMap<String,Object> result = (HashMap<String, Object>) array.get("type");
             result = (HashMap<String, Object>) result.get("array"); // Second array
             result = (HashMap<String, Object>) result.get("type"); //Third element info

             if (result.containsKey("record")) //
             {
                 HashMap<String,Object> temp = (HashMap<String, Object>) array.get("type");
                 if (accessMembers.size()==2)
                     return "record";
                 if (!accessMembers.get(2).get("type").equals("dot")) throw new WrongSyntaxException("Can't access record as an array");
                 //Peel off one access members and make a recursive call on member's members(skip member access)
                 ArrayList<HashMap<String,Object>> subMembers = new ArrayList<>(accessMembers.subList(2, accessMembers.size()));
                 return getRecordType(getMembers((HashMap<String, Object>) temp.get("array")), subMembers);
             }
             else if (result.containsKey("primitive"))
             {
                 if (accessMembers.size()>3) throw new SyntaxException("Modifiable does not have member: " + accessMembers.get(2).get("value"));
                 //If member is primitive
                 return (String) result.get("primitive");
             }
             else if (result.containsKey("array"))
             {

                 HashMap<String,Object> temp = (HashMap<String, Object>) result.get("array"); //Third array
                 HashMap<String,Object> element = (HashMap<String, Object>) temp.get("type"); //Next element info

                 if (accessMembers.size()==2)
                     return "array";
                // if (!element.containsKey("array") && accessMembers.size()!=3) throw new WrongSyntaxException("Array has no element: " + accessMembers.get(3));

                 if(element.containsKey("array"))
                 {
                     if (!accessMembers.get(2).get("type").equals("expression")) throw new WrongSyntaxException("Can't access array as record");

                     //If there are 3 array access members in a row check third index validity and return array
                     if (accessMembers.size()==3)
                     {
                         //Check index validity
                         HashMap<String, Object> index = accessMembers.get(2);
                         String expressionResult = calculateExpressionResult(index.get("value"));
                         if (!expressionResult.equals("integer"))throw new TypeMismatch("Array element access expression is not integer");
                         return "array";
                     }
                     else
                     {
                         ArrayList<HashMap<String,Object>> subMembers = new ArrayList<>(accessMembers.subList(2, accessMembers.size()));
                         return getArrayType(temp, subMembers);
                     }
                 } else if(element.containsKey("record"))
                 {
                     if (!accessMembers.get(3).get("type").equals("dot")) throw new WrongSyntaxException("Can't access array as record");

                     //Check index validity
                     HashMap<String, Object> index = accessMembers.get(2);
                     String expressionResult = calculateExpressionResult(index.get("value"));
                     if (!expressionResult.equals("integer"))throw new TypeMismatch("Array element access expression is not integer");

                     if (accessMembers.size()==3)
                         return "record";
                     ArrayList<HashMap<String,Object>> subMembers = new ArrayList<>(accessMembers.subList(3, accessMembers.size()));
                     return getRecordType(getMembers(temp), subMembers);
                 }
                 else if(element.containsKey("primitive"))
                 {
                     if (accessMembers.size()>3) throw new SyntaxException("Modifiable does not have member: " + accessMembers.get(1).get("value"));

                     //Check index validity
                     HashMap<String, Object> index = accessMembers.get(2);
                     String expressionResult = calculateExpressionResult(index.get("value"));
                     if (!expressionResult.equals("integer"))throw new TypeMismatch("Array element access expression is not integer");

                     //If member is primitive
                     return (String) element.get("primitive");
                 } else throw new SyntaxException("Invalid modifiable array syntax");
             } else throw new SyntaxException("Invalid modifiable array syntax");
        } else throw new TypeMismatch("Array element access expression is not integer");

    }

    //just go down the tree and get the type of expression
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
                else {
                    //Get member access list
                    ArrayList<HashMap<String, Object>> mods = (ArrayList<HashMap<String, Object>>) modifvar.get("mods");
                    String type = "";
                    ArrayList<HashMap<String, Object>> members = null;
                    //Check if modifiable exists
                    if (symbolsDeclarations.containsKey(modifName))
                    {
                        if (mods.get(0).get("type").equals("dot"))
                        {
                            //Get modifiable members
                            members = getMembers((HashMap<String, Object>) symbolsDeclarations.get(modifName).getUnit());
                            //Check if access is possible and get type of the member
                            type = getRecordType(members, mods);
                        } else if (mods.get(0).get("type").equals("expression"))
                            //Check if access is possible and get type of the member
                            type = getArrayType(modifvar, mods);
                        return type;
                    }
                    else throw new SyntaxException("Undeclared record: " + modifName);

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


}
