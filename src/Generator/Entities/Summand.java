package Generator.Entities;

import java.util.HashMap;

public class Summand {
    private Primary mainPrimary;
    private Expression mainExpression;

    public Summand(HashMap<String, Object> mp) {
        HashMap<String, Object> map = (HashMap<String, Object>)mp.get("left");
//        if (map.get("is").equals("primary")) mainPrimary = new Primary((HashMap<String, Object>) map.get("value"));
        if (map.get("is").equals("primary")) mainPrimary = new Primary((HashMap<String, Object>) map);
        else if (map.get("is").equals("expression")) mainExpression = new Expression((HashMap<String, Object>) map.get("value"));
    }

    public String toJavaCode() {
        StringBuilder result = new StringBuilder();
        if (mainExpression != null)
            return result.append("(").append(mainExpression.toJavaCode()).append(")").toString();
        return result.append(mainPrimary.toJavaCode()).toString();
    }
}
