package Generator.Entities;

import Generator.Types.Type;

public class Identifier implements Type{
    private String name;

    public Identifier(String name) {
        this.name = name;
    }

    public String toJavaCode() {
        return name;
    }
}
