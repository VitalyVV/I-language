package Syntax;

import Generator.Entities.Identifier;
import Generator.Types.PrimitiveType;

import java.util.ArrayList;
import java.util.HashMap;

public class TreeTranslator {

    ArrayList<HashMap<String,Object>> program;
    ArrayList<Object> translation;
    public TreeTranslator(HashMap<String,Object> tree)
    {
        program = (ArrayList<HashMap<String, Object>>) tree.get("Root");
    }

    public ArrayList<Object> translate() throws Exception {
        for (HashMap<String,Object> element: program)
        {
            HashMap<String,Object> content = (HashMap<String,Object>) element.get("Content");
            if (content.get("statement").equals("var")) //Var declaration
            {
                HashMap<String,Object> type = (HashMap<String, Object>) content.get("type");
                //Check usertypes TODO
                if (type.containsKey("record"))
                {

                } else if (type.containsKey("primitive"))
                {
                    //PrimitiveType primType = new PrimitiveType((String) type.get("primitive"));
                    //Identifier id = new Identifier((String) type.get("name"));
                    //translation.add();
                } else if (type.containsKey("array"))
                {

                }


                return null;
            }
            else if (content.get("statement").equals("declaration")) //Routine declaration
            {
                return null;

            } else if (content.get("statement").equals("type")) //Routine declaration
            {
                return null;

            }else throw new Exception("Invalid syntax structure");
        }
        return translation;
    }

}
