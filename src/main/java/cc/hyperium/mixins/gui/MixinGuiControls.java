package cc.hyperium.mixins.gui;

import cc.hyperium.gui.keybinds.GuiKeybinds;
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiControls;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.List;

@Mixin(GuiControls.class)
public class MixinGuiControls extends GuiScreen {
    @Inject(method = "initGui", at = @At("RETURN"))
    private void initGui(CallbackInfo ci) {
        buttonList.add(new GuiButton(10, ResolutionUtil.current().getScaledWidth() / 2 - 60, ResolutionUtil.current().getScaledHeight() - 10, 120, 10, "Hyperium Binds"));
    }

    @Inject(method = "actionPerformed", at = @At("HEAD"))
    private void actionPerformed(GuiButton button, CallbackInfo ci) {
        if (button.id == 10) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiKeybinds());
        }
    }
}
