package cc.hyperium.event.gui.container;

import cc.hyperium.event.Event;

/**
 * Called at the HEAD of {@code GuiContainer#keyTyped}.
 *
 * Warning: please don't use this unless you know what you are doing. This event has
 * been added for a very, very specific use case.
 */
public class GuiContainerKeyTypedHead extends Event {
    public final int keyCode;

    public GuiContainerKeyTypedHead(int keyCode) {
        this.keyCode = keyCode;
    }
}
