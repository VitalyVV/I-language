package helpers;

import Syntax.WrongSyntaxException;
import com.sun.corba.se.impl.io.TypeMismatchException;
import jdk.nashorn.internal.runtime.regexp.joni.exception.SyntaxException;
import org.omg.IOP.CodecPackage.TypeMismatch;

import javax.naming.OperationNotSupportedException;
import java.util.*;

public abstract class Node {

    private String name;
    protected int currentChild = 0;

    protected Symbol lastKnown = null;


    protected ArrayList<HashMap<String, Object>> children;

    //list of variable symbols only
    protected LinkedHashMap<String, Symbol> symbolsDeclarations = new LinkedHashMap<>();

    //type declarations
    protected HashMap<String,Object> typeMappings = new LinkedHashMap<>();
    protected HashMap<String,Object> memberMappings = new HashMap<>();

    protected HashMap<String, HashMap<String, Object>> routineEndTypes = new HashMap<>();

    Iterator<Map.Entry<String,Symbol>>entries;
    //list of routines only
    protected LinkedList<RoutineNode> routines = new LinkedList<>();
    protected LinkedList<String> namesRoutines = new LinkedList<>();

    Node (String name){
        this.name = name;
    }


    public String getName(){
        return name;
    }


    //For passing the types declared in parent before entering the sub-scope
    protected HashMap<String, Object> getLastKnownType(){

        HashMap<String, Object> typesExisted = new HashMap<>();
        Map.Entry<String,Object> lastElement;
        Iterator<Map.Entry<String,Object>> types = typeMappings.entrySet().iterator();
        while (types.hasNext()) {
            lastElement = types.next();
            typesExisted.put(lastElement.getKey(), lastElement.getValue());
        }
        return typesExisted;
    }

    public HashMap<String, Object> getRoutineType(String nameR){
        return routineEndTypes.get(nameR);
    }

    public Map.Entry<String, Symbol> getChild(){

        if (currentChild>=symbolsDeclarations.size()) return null;
        Map.Entry<String,Symbol> entry = entries.next();
        currentChild++;
        //update the pointer for routine we have not visited before
        return entry;
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
        if (a.equals("integer") && b.equals("integer") && op.equals("div") ) return "real";
        else if (a.equals("integer") && b.equals("integer") && getOpType(op).equals("simple")) return "boolean";
        else if (a.equals("integer") && b.equals("integer")) return "integer";
        else if ((a.equals("integer") && b.equals("boolean")) || (a.equals("boolean") && b.equals("integer"))) return "integer";
        else if (a.equals("real") && b.equals("real")) return "real";
        else if (a.equals("boolean") && b.equals("boolean")) return "boolean";
        else if ((a.equals("real") && b.equals("integer")) || (a.equals("integer") && b.equals("real"))) return "real";
        else throw new Exception("Incompatible types: "+a+", "+b+ " with operand "+op);
    }

    protected boolean getAssignmentresult(String modPrim, String toAssign) throws Exception{
        if (modPrim.equals("integer") && toAssign.equals("integer")){
            return true;
        }
        else if (modPrim.equals("integer") && toAssign.equals("real")){
            return true;
        }
        else if (modPrim.equals("integer") && toAssign.equals("boolean")){
            return true;
        }
        else if (modPrim.equals("real") && toAssign.equals("real")){
            return true;
        }
        else if (modPrim.equals("real") && toAssign.equals("boolean")){
            return true;
        }
        else if (modPrim.equals("boolean") && toAssign.equals("boolean")){
            return true;
        }
        else if (modPrim.equals("boolean") && toAssign.equals("integer")){
            return true;
        }
        else if (modPrim.equals(toAssign)){
            return true;
        }
        else throw new Exception("Incompatible types: "+modPrim+", "+toAssign+ " for assignment");
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
                return (String) typeMappings.get(typeVarName);
            } else throw new Exception("No such identifier declared: "+typeVarName);
        }
        else if (a.containsKey("array")){
            return "array";
        }
        else if (a.containsKey("record")){
            return "record";
        }
        else if (a.containsKey("type")){
            return getType(a.get("type"));
        }
        else {
            Map.Entry<String,Object> entry = a.entrySet().iterator().next();
            throw  new Exception("No such type exists: "+entry.getKey());
        }
    }

    protected boolean isIntBooleanable(HashMap<String,Object> value)
    {
        HashMap<String,Object> val = value;
        while(true)
        {
            if (val.containsKey("type") && val.get("type").equals("integer")) break;
            if (val.containsKey("hasright") && val.get("hasright").equals("true")) return false;
            if (val.containsKey("type") && val.get("type").equals("modifiable")) return false;
            val = (HashMap<String, Object>) val.get("left");
        }
        if (val.containsKey("sign") && val.get("sign").equals("-") && val.get("value").equals("1")) return false;
        if (!(val.get("value").equals("1") || val.get("value").equals("0"))) return false;
        return true;
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

    protected String getArrayType(HashMap<String,Object> array, ArrayList<HashMap<String,Object>> accessMembers) throws Exception {
        if (accessMembers.size()==1) {
            HashMap<String, Object> index = accessMembers.get(0);
            String expressionResult = calculateExpressionResult(index.get("value"));
            if (expressionResult.equals("integer")) {
                HashMap<String,Object> temp = (HashMap<String, Object>) array.get("array");
                HashMap<String,Object> result  = (HashMap<String, Object>) temp.get("type");
                if (result.containsKey("record")) return "record";
                else if (result.containsKey("primitive")) return (String) result.get("primitive");
                else if (result.containsKey("array")) return "array";
                else throw new SyntaxException("Invalid member access syntax");

            } else throw new TypeMismatch("Array element access expression is not integer");
        }


        if (accessMembers.get(0).get("type").equals("expression") && !accessMembers.get(1).get("type").equals("expression"))
        {
            HashMap<String, Object> index = accessMembers.get(0);
            String expressionResult = calculateExpressionResult(index.get("value"));
            if (expressionResult.equals("integer")) {
                HashMap<String,Object> temp = (HashMap<String, Object>) array.get("array");
                HashMap<String,Object> result  = (HashMap<String, Object>) temp.get("type");
                if (result.containsKey("record"))
                {
                    if (accessMembers.size()==1) return "record";
                    ArrayList<HashMap<String,Object>> subMembers = new ArrayList<>(accessMembers.subList(1, accessMembers.size()));
                    return getRecordType(getMembers(temp), subMembers);
                }
                else if (result.containsKey("primitive")) throw new SyntaxException("Primitive has of type " + ((String) result.get("primitive")) + " has no members.");
                else if (result.containsKey("array")) throw new SyntaxException("Invalid array access syntax");
                else throw new SyntaxException("Invalid member access syntax");

            } else throw new TypeMismatch("Array element access expression is not integer");
        }

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

    protected String getRecordType(ArrayList<HashMap<String,Object>> recordMembers, ArrayList<HashMap<String,Object>> accessMembers) throws Exception {
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


    //Returns members of record hashmap
    protected ArrayList<HashMap<String,Object>> getMembers(HashMap<String,Object> unit)
    {
        HashMap<String,Object> type = (HashMap<String,Object>) unit.get("type");
        if (type.containsKey("record"))
        {
            HashMap<String,Object> record = (HashMap<String,Object>) type.get("record");
            ArrayList<HashMap<String,Object>> members = (ArrayList<HashMap<String,Object>>) record.get("content");
            return members;
        } else if (type.containsKey("identifier"))
        {
            if(memberMappings.containsKey(type.get("identifier")))
            {
                HashMap<String,Object> record = (HashMap<String,Object>) memberMappings.get(type.get("identifier"));
                record = (HashMap<String, Object>) record.get("record");
                ArrayList<HashMap<String,Object>> members = (ArrayList<HashMap<String,Object>>) record.get("content");
                return members;
            } else throw new SyntaxException("Not a record: "+ unit.get("name"));
        }
        else throw new SyntaxException("Not a record: "+ unit.get("name"));
    }

    protected String getModifiableType(HashMap<String, Object> modifvar) throws Exception {
        String modifName = (String) modifvar.get("value");
        if (((ArrayList<HashMap<String, Object>>)modifvar.get("mods")).isEmpty()) {
            if (symbolsDeclarations.keySet().contains(modifName)){
                return symbolsDeclarations.get(modifName).getType();
            }
            else if (namesRoutines.contains(modifName)){
                RoutineSymbol routine = (RoutineSymbol) symbolsDeclarations.get(modifName).getUnit();
                return routine.getType();
            }
            else throw new Exception("No such identifier declared: "+modifName);
        }
        else {
            //Get member access list
            ArrayList<HashMap<String, Object>> mods = (ArrayList<HashMap<String, Object>>) modifvar.get("mods");
            String type = "";
            ArrayList<HashMap<String, Object>> members = null;
            HashMap<String,Object> array = null;
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
                {
                    array = (HashMap<String, Object>) symbolsDeclarations.get(modifName).getUnit();
                    HashMap<String,Object> typ = (HashMap<String, Object>) array.get("type");
                    if (typ.containsKey("identifier"))
                    {
                        if (memberMappings.containsKey(typ.get("identifier")))
                        {
                            type = getArrayType((HashMap<String,Object>) memberMappings.get(typ.get("identifier")), mods);
                        }  else throw new SyntaxException("Undeclared type mapping for: " + typ.get("identifier"));
                    } else
                    //Check if access is possible and get type of the member
                    type = getArrayType((HashMap<String, Object>) array.get("type"), mods);
                }
                return type;
            }
            else throw new SyntaxException("Undeclared record: " + modifName);
        }
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
                return getModifiableType((HashMap<String, Object>) hashmaped.get("value"));
            }
            else if (typePrimary.equals("routinecall")){
                HashMap<String, Object> hashmapedRotineCall = (HashMap<String, Object>) hashmaped.get("value");
                String name = (String)hashmapedRotineCall.get("variable");
                if (namesRoutines.contains(name)){
                    int index = namesRoutines.indexOf(name);
                    RoutineNode rnode = routines.get(index);
                    ArrayList<HashMap<String,Object>> params = (ArrayList<HashMap<String, Object>>) hashmapedRotineCall.get("parameters");
                    rnode.checkCompatibility(params);
                    return rnode.getResultType();
                } else throw new Exception("Called routine was not declared "+ name);
            }
            else throw new Exception("No such type exists: " + typePrimary);
        }

        String result = calculateExpressionResult(hashmaped.get("left"));
        String mapedResultType  = (String)hashmaped.get("is");
        //Logic for factor relation and simple type is identical since all require same type left and right operands
        if (mapedResultType.equals("factor") || mapedResultType.equals("relation") || mapedResultType.equals("simple") || mapedResultType.equals("expression") || mapedResultType.equals("summand")){
            if (hashmaped.containsKey("right")){
                String resultRight = calculateExpressionResult(hashmaped.get("right"));
                return getTypesResult(result, resultRight, (String)hashmaped.get("op"));
            } else return result;
            //Summand and expression have only left operands
        }
        else throw new TypeMismatchException("Syntax analysis failed. Expression type not valid: "+ (String)hashmaped.get("op"));
    }

    void parseBody(ArrayList<HashMap<String, Object>> body) throws Exception{
        for (HashMap<String, Object> elem: body){

            //Get statement content
            HashMap<String, Object> cont = (HashMap<String, Object>) elem.get("Content");

            if (((String)cont.get("statement")).equals("call")){ //If function call
                String name = (String) cont.get("variable");
                if (symbolsDeclarations.containsKey(name) && symbolsDeclarations.get(name).getType().equals("routine")){
                    Symbol routineS = symbolsDeclarations.get(name);
                    RoutineNode rnode = (RoutineNode) routineS.getUnit();
                    if (cont.keySet().contains("parameters")){
                        //check that the function we call already exists, and number ant types of parameters we pass match
                        ArrayList<HashMap<String, Object>> params = (ArrayList<HashMap<String, Object>>) cont.get("parameters");
                        rnode.checkCompatibility(params);
                    }
                }
                else
                    throw new Exception("No such identifier previously declared: "+name);
            } else if (cont.get("statement").equals("assignment")){ //check assignment correctness
                String toAssign = calculateExpressionResult(cont.get("value")); //Assignment value
                String assignableType = getModifiableType((HashMap<String, Object>) cont.get("name"));

                if (assignableType.equals("boolean") &&  toAssign.equals("integer") && !isIntBooleanable((HashMap<String, Object>) cont.get("value"))) throw new WrongSyntaxException("Can't assign non 1 or 0 int to boolean");
                if (!assignableType.equals(toAssign) && !(assignableType.equals("boolean") &&  toAssign.equals("integer")))
                {
                    HashMap<String,Object> assignable = (HashMap<String, Object>) cont.get("name");
                    String assignableName = (String) assignable.get("value");
                    throw new WrongSyntaxException("Incompatible assignment type for variable \""+assignableName+"\" expected "+assignableType);
                }
                HashMap<String,Object> assignable = (HashMap<String, Object>) cont.get("name");
                String assignableName = (String) assignable.get("value");
                lastKnown = symbolsDeclarations.get(assignableName);
                //Check for assignment result according to obtained types, if no match - exception is thrown

                //Create new nodes on current scope with expression checking in the node class TODO
            } else if (cont.get("statement").equals("if")){
                IfNode ifnode = new IfNode("if", symbolsDeclarations, typeMappings, cont);
                lastKnown = new Symbol("if", "if", null);

            }

            //create new subscope for
            else if (cont.get("statement").equals("for")){
                ForLoopNode fornode = new ForLoopNode("for", symbolsDeclarations,typeMappings, cont);
                lastKnown = new Symbol("for", "for", null);
            }

            //create new subscope while
            else if (cont.get("statement").equals("while")){
                WhileNode whilenode = new WhileNode("while", symbolsDeclarations,typeMappings, cont);
                lastKnown = new Symbol("while", "while", null);
            }


            else if (cont.get("statement").equals("var")){ //If variable declaration
                if (symbolsDeclarations.containsKey(cont.get("name"))) throw new WrongSyntaxException("Variable has already been declared: "+cont.get("name"));
                if (typeMappings.containsKey(cont.get("name"))) throw new SyntaxException("Type has already been declared: "+ cont.get("name") +" can't create a variable with such name.");

                if (cont.get("hastype").equals("true")) {
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
                    lastKnown = s;
                } else {
                    //if type is declared via expression calculation like "var a: integer is (5+5)*10"
                    String valueType = calculateExpressionResult(cont.get("expression"));
                    Symbol s = new Symbol(valueType, (String) cont.get("name"), cont);
                    symbolsDeclarations.put((String) cont.get("name"), s);
                }
            }
            else if (cont.get("statement").equals("type")){
                //Add type mapping to the listvar e: integer is bar(10.1)
                if (typeMappings.keySet().contains(cont.get("name"))) throw new WrongSyntaxException("Type has already been declared: "+ cont.get("name"));
                if (symbolsDeclarations.containsKey(cont.get("name"))) throw new SyntaxException("Variable has already been declared: "+ cont.get("name")+" can't call a new type with such name");

                typeMappings.put((String) cont.get("name"), getType(cont.get("type")));
                if (getType(cont.get("type")).equals("record") || getType(cont.get("type")).equals("array"))
                {
                    memberMappings.put((String) cont.get("name"), cont.get("type"));
                }
            } else throw new WrongSyntaxException("Unknown statement type: "+ cont.get("statement"));
        }

    }

}

