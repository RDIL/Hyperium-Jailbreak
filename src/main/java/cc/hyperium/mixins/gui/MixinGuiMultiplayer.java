package cc.hyperium.mixins.gui;

import cc.hyperium.mixinsimp.renderer.gui.IMixinGuiMultiplayer;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.multiplayer.ServerData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GuiMultiplayer.class)
public abstract class MixinGuiMultiplayer implements IMixinGuiMultiplayer {
    @Shadow
    private boolean directConnect;

    @Override
    public void makeDirectConnect() {
        directConnect = true;
    }

    @Shadow
    private ServerData selectedServer;

    @Override
    public void setIp(ServerData ip) {
        this.selectedServer = ip;
    }
}
