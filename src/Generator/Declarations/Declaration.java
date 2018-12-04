package Generator.Declarations;

import java.util.ArrayList;

/**
 * Created by Алек on 04.12.2018.
 */
public interface Declaration {
    class Body {
        private ArrayList<BodyElement> elements;

        public Body(ArrayList<BodyElement> elements) {
            this.elements = elements;
        }

        public ArrayList<BodyElement> getElements() {
            return elements;
        }
    }

    interface BodyElement{
    }
}
