package cc.hyperium.mixins.gui;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.IInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Used externally, requested by multiple addon authors to be moved into the core.
 */
@Mixin(GuiChest.class)
public interface IMixinGuiChest {
    @Accessor IInventory getUpperChestInventory();
    @Accessor IInventory getLowerChestInventory();
}
