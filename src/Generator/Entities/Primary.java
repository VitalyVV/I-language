package Generator.Entities;

import Generator.Statements.RoutineCall;

public class Primary {
    private Sign sign;
    private IntegerLiteral mainIntegerLiteral;
    private RealLiteral mainRealLiteral;
    private boolean mainBooleanLiteral;
    private ModifiablePrimary mainModifiablePrimary;
    private RoutineCall mainRoutineCall;

    public Primary(Sign sign, IntegerLiteral integerLiteral) {
        this.sign = sign;
        this.mainIntegerLiteral = integerLiteral;
    }

    public Primary(Sign sign, RealLiteral realLiteral) {
        this.sign = sign;
        this.mainRealLiteral = realLiteral;
    }

    public Primary(boolean mainBooleanLiteral) {
        this.mainBooleanLiteral = mainBooleanLiteral;
    }

    public Primary(ModifiablePrimary mainModifiablePrimary) {
        this.mainModifiablePrimary = mainModifiablePrimary;
    }

    public Primary(RoutineCall mainRoutineCall) {
        this.mainRoutineCall = mainRoutineCall;
    }

    public String toJavaCode() {
        StringBuilder result = new StringBuilder();
        if (mainIntegerLiteral != null) {
            if (sign.toJavaCode().equals("+")) return result.append(mainIntegerLiteral.getValue()).toString();
            else return result.append(sign.toJavaCode()).append(mainIntegerLiteral.getValue()).toString();
        } else if (mainRealLiteral != null) {
            if (sign.toJavaCode().equals("+")) return result.append(mainRealLiteral.getValue()).toString();
            else return result.append(sign.toJavaCode()).append(mainRealLiteral.getValue()).toString();
        } else if (mainModifiablePrimary != null) return mainModifiablePrimary.toJavaCode();
        else if (mainRoutineCall != null) return mainRoutineCall.toJavaCode();
        return String.valueOf(mainBooleanLiteral);
    }
}
