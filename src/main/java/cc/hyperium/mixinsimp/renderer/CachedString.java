package cc.hyperium.mixinsimp.renderer;

public class CachedString {
    private String text;
    private float width;
    private float height;

    public CachedString(String text, float width, float height) {
        this.text = text;
        this.width = width;
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float v) {
        this.width = v;
    }

    public float getHeight() {
        return height;
    }

    public String getText() {
        return text;
    }
}
