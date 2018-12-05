package Generator.Entities;

public class Primary {
    private Sign sign;
    private IntegerLiteral mainIntegerLiteral;
    private RealLiteral mainRealLiteral;
    private boolean mainBooleanLiteral;
    private ModifiablePrimary mainModifiablePrimary;

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

    public Sign getSign() {
        return sign;
    }

    public IntegerLiteral getMainIntegerLiteral() {
        return mainIntegerLiteral;
    }

    public RealLiteral getMainRealLiteral() {
        return mainRealLiteral;
    }

    public boolean isMainBooleanLiteral() {
        return mainBooleanLiteral;
    }

    public ModifiablePrimary getMainModifiablePrimary() {
        return mainModifiablePrimary;
    }
}
