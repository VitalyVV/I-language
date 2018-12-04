package Generator.Entities;

public class Sign {
    private String sign;

    public Sign(String sign) {
        if (!(sign.equals("+") || sign.equals("-"))) {
            throw new RuntimeException("Not a sign: " + sign);
        }
        this.sign = sign;
    }

    public String getSign() {
        return sign;
    }
}
