package Generator.Entities;

import Generator.Statements.RoutineCall;

import java.util.HashMap;

public class Primary {
    private Sign sign;
    private IntegerLiteral mainIntegerLiteral;
    private RealLiteral mainRealLiteral;
    private boolean mainBooleanLiteral;
    private ModifiablePrimary mainModifiablePrimary;
    private RoutineCall mainRoutineCall;

    public Primary(HashMap<String, Object> map) {
        if (map.get("type").equals("real")) {
            if (map.containsKey("sign")) sign = new Sign((String) map.get("sign"));
            mainRealLiteral = new RealLiteral(Double.parseDouble((String) map.get("value")));
        } else if (map.get("type").equals("integer")) {
            if (map.containsKey("sign")) sign = new Sign((String) map.get("sign"));
            mainIntegerLiteral = new IntegerLiteral(Integer.valueOf((String) map.get("value")));
        } else if (map.get("type").equals("boolean")){
            mainBooleanLiteral = Boolean.valueOf((String) map.get("value"));
        } else if (map.get("type").equals("modifiable"))
            mainModifiablePrimary = new ModifiablePrimary((HashMap<String, Object>) map.get("value"));
    }

    public String toJavaCode() {
        StringBuilder result = new StringBuilder();
        if (mainIntegerLiteral != null) {
            if (sign != null) {
                if (sign.toJavaCode().equals("+")) return result.append(mainIntegerLiteral.getValue()).toString();
                else return result.append(sign.toJavaCode()).append(mainIntegerLiteral.getValue()).toString();
            } else {
                return result.append(mainIntegerLiteral.getValue()).toString();
            }
        } else if (mainRealLiteral != null) {
            if (sign != null) {
                if (sign.toJavaCode().equals("+")) return result.append(mainRealLiteral.getValue()).toString();
                else return result.append(sign.toJavaCode()).append(mainRealLiteral.getValue()).toString();
            } else {
                return result.append(mainRealLiteral.getValue()).toString();
            }
        } else if (mainModifiablePrimary != null) return mainModifiablePrimary.toJavaCode();
        else if (mainRoutineCall != null) return mainRoutineCall.toJavaCode();
        return String.valueOf(mainBooleanLiteral);
    }
}
