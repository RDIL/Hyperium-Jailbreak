package cc.hyperium.event.render;

import cc.hyperium.event.Event;
import net.minecraft.item.ItemStack;

/**
 * Allows for observing and setting the value of an item's durability.
 */
public class ItemGetDurabilityEvent extends Event {
    public final ItemStack item;
    public double durability;

    public ItemGetDurabilityEvent(ItemStack item, double durability) {
        this.item = item;
        this.durability = durability;
    }
}
