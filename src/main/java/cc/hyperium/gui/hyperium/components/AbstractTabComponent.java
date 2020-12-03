package cc.hyperium.gui.hyperium.components;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public abstract class AbstractTabComponent {
    public boolean hover;
    /**
     * @deprecated Don't use this anymore, it doesn't do anything.
     */
    @Deprecated
    protected List<String> tags = new ArrayList<>();
    protected AbstractTab tab;
    private List<Consumer<Object>> stateChanges = new ArrayList<>();
    private boolean enabled = true;

    public AbstractTabComponent(AbstractTab tab, List<String> tags) {
        this.tab = tab;
    }

    public int getHeight() {
        return 18;
    }

    public void render(int x, int y, int width, int mouseX, int mouseY) {
        GlStateManager.pushMatrix();
        if (hover)
            Gui.drawRect(x, y, x + width, y + 18, 0xa0000000);
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

    /**
     * @deprecated Don't use this, tags no longer have an effect.
     * @param ts The tags to add.
     * @return The tab component.
     */
    @Deprecated
    public AbstractTabComponent tag(String... ts) {
        for (String string : ts) {
            tags.add(string.toLowerCase());
        }
        return this;
    }

    /**
     * @deprecated Don't use this, tags no longer have an effect.
     * @param ts The tags to add.
     * @return The tab component.
     */
    @Deprecated
    public AbstractTabComponent tag(List<String> ts) {
        tags.addAll(ts.stream().map(String::toLowerCase).collect(Collectors.toList()));
        return this;
    }
}
