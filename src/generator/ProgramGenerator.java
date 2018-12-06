package generator;

import java.util.*;

//I think that functions for processing

public class ProgramNode extends Generator {

    ArrayList<HashMap<String, Object>> elements;

    public ProgramNode(String name, ArrayList<HashMap<String, Object>> elements){
        super(name);
        this.elements = elements;
    }

    //Start method to go down the tree and get the types of objects
    public String generateRoot() throws Exception{
        for (HashMap<String, Object> elem: elements){

            HashMap<String, Object> rootUnit = ((HashMap<String, Object>) elem.get("Content"));
            String unit = (String) rootUnit.get("statement");
            if (unit.equals("var")) {// Variable declaration type extraction
                String type = getType(rootUnit.get("type")); // Get type template or create object with template
                String name = (String) rootUnit.get("name");
                String expression = generateExpression(rootUnit.get("value")); //  equivalent to calculate expression result

                //Add to body

            } else if(unit.equals("declaration")){
                RoutineGenerator rg = new RoutineGenrator(unit.get(value));
                String routineBody = rg.generateBody(); // equivalent to parseBody()

                //add to body

            }else if(unit.equals("type")){//if we have type declaration
                //Generate object generation template
            }
        }

    }

}
