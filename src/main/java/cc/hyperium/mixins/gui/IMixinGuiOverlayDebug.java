package cc.hyperium.mixins.gui;

import net.minecraft.client.gui.GuiOverlayDebug;
import net.minecraft.client.gui.ScaledResolution;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GuiOverlayDebug.class)
public interface IMixinGuiOverlayDebug {
    @Invoker void callRenderDebugInfoLeft();
    @Invoker void callRenderDebugInfoRight(ScaledResolution sr);
}
