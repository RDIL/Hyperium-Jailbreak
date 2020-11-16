package cc.hyperium.gui.hyperium;

import java.lang.reflect.Field;

public class RGBFieldSet {
    private Field red;
    private Field green;
    private Field blue;
    private Object parentObj;

    public Object getParentObj() {
        return parentObj;
    }

    public RGBFieldSet(Field red, Field green, Field blue, Object parentObj) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.parentObj = parentObj;
    }

    public Field getRed() {
        return red;
    }

    public Field getGreen() {
        return green;
    }

    public Field getBlue() {
        return blue;
    }
}
