package cc.hyperium.mixins.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GuiScreen.class)
public interface IMixinGuiScreen {
    @Accessor
    FontRenderer getFontRendererObj();
}
