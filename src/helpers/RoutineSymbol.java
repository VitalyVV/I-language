package helpers;

import java.util.ArrayList;
import java.util.HashMap;

public class RoutineSymbol extends Symbol {

    private final ArrayList<HashMap<String, Object>> params;
    public RoutineSymbol(String  type, Object c, String name, ArrayList<HashMap<String, Object>> params){
        super(type, name, c);
        this.params = params;
    }
}
