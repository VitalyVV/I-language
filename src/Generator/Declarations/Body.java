package Generator.Declarations;

import java.util.ArrayList;

public class Body {
    private ArrayList<BodyElement> elements;

    public Body(ArrayList<BodyElement> elements) {
        this.elements = elements;
    }

    public String toJavaCode() {
        StringBuilder result = new StringBuilder();
        for (BodyElement element : elements
                ) {
            result.append(element).append("\n");
        }
        return result.toString();
    }

    public ArrayList<BodyElement> getElements() {
        return elements;
    }
}