package cc.hyperium.gui.hyperium.components;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class AbstractTabComponent {
    public boolean hover;
    private List<Consumer<Object>> stateChanges = new ArrayList<>();
    private boolean enabled = true;

    public AbstractTabComponent() {
    }

    public int getHeight() {
        return 18;
    }

    public void render(int x, int y, int width, int mouseX, int mouseY) {
        GlStateManager.pushMatrix();
        if (hover) {
            Gui.drawRect(x, y, x + width, y + 18, 0xa0000000);
        }
        GlStateManager.popMatrix();
    }

    public void onClick(int x, int y) {}

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void registerStateChange(Consumer<Object> objectConsumer) {
        stateChanges.add(objectConsumer);
    }

    protected void stateChange(Object o) {
        for (Consumer<Object> tmp : stateChanges) {
            tmp.accept(o);
        }
    }

    public void mouseEvent(int x, int y) {}
}
