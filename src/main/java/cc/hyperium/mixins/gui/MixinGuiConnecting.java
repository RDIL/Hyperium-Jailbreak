/*
 *     Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.mixins.gui;

import cc.hyperium.event.EventBus;
import cc.hyperium.event.network.server.ServerJoinEvent;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.NetworkManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiConnecting.class)
public abstract class MixinGuiConnecting extends GuiScreen {
    private int frame;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void constructorInjection(CallbackInfo callbackInfo) {
        this.frame = 0;
    }

    @Shadow private NetworkManager networkManager;

    @Inject(method = "connect", at = @At("HEAD"))
    private void connect(String ip, int port, CallbackInfo ci) {
        EventBus.INSTANCE.post(new ServerJoinEvent(ip, port));
    }

    /**
     * @author Reece Dunham and Mojang
     */
    @Overwrite
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        String[] frames = new String[]{"|", "/", "-", "\\"};

        this.drawWorldBackground(0);

        this.frame = (this.frame == 3? 0 : this.frame + 1);
        this.drawCenteredString(this.fontRendererObj, I18n.format(this.networkManager == null? "connect.connecting" : "connect.authorizing"), this.width / 2, this.height / 2 - 50, 16777215);
        this.drawCenteredString(this.fontRendererObj, frames[this.frame], this.width / 2, this.height / 2 - 30, 16777215);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
