package cc.hyperium.event.render;

import cc.hyperium.event.Event;
import net.minecraft.item.ItemStack;

/**
 * Allows for observing and setting the value of if an item is damaged or not.
 */
public class ItemIsDamagedEvent extends Event {
    public final ItemStack item;
    public boolean isDamaged;

    public ItemIsDamagedEvent(ItemStack item, boolean damaged) {
        this.item = item;
        this.isDamaged = damaged;
    }
}
