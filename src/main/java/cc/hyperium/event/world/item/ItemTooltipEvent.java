package cc.hyperium.event.world.item;

import java.util.List;

import cc.hyperium.event.Event;
import net.minecraft.item.ItemStack;

public class ItemTooltipEvent extends Event {
    private List<String> tooltip;
    private final ItemStack stack;

    public ItemTooltipEvent(ItemStack stack, List<String> tooltip) {
        this.stack = stack;
        this.tooltip = tooltip;
    }

    public ItemStack getStack() {
        return this.stack;
    }

    public List<String> getTooltip() {
        return this.tooltip;
    }

    public void setTooltip(List<String> tooltip) {
        this.tooltip = tooltip;
    }
}
