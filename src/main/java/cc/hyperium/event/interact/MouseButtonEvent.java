package cc.hyperium.event.interact;
import cc.hyperium.event.Event;

public class MouseButtonEvent extends Event {
    private final int value;
    private final boolean state;

    public MouseButtonEvent(int value, boolean state) {
        this.value = value;
        this.state = state;
    }

    public int getValue() {
        return this.value;
    }

    public boolean getState() {
        return this.state;
    }
}
