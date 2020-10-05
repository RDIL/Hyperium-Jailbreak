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

package cc.hyperium.mixins.packet;

import cc.hyperium.Hyperium;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.network.chat.ServerChatEvent;
import cc.hyperium.mods.timechanger.TimeChanger;
import com.google.common.collect.ObjectArrays;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.client.C19PacketResourcePackStatus;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.network.play.server.S48PacketResourcePackSend;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumParticleTypes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Mixin(NetHandlerPlayClient.class)
public abstract class MixinNetHandlerPlayClient {
    @Shadow private WorldClient clientWorldController;
    @Shadow @Final private NetworkManager netManager;
    @Shadow private Minecraft gameController;
    @Shadow public abstract NetworkManager getNetworkManager();

    private TimeChanger timeChanger = Hyperium.INSTANCE.getModIntegration().getTimeChanger();

    @ModifyArg(method = "handleTabComplete", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiChat;onAutocompleteResponse([Ljava/lang/String;)V"))
    private String[] addClientTabCompletions(String[] currentCompletions) {
        String[] modCompletions = Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().getLatestAutoComplete();
        if (modCompletions != null) {
            currentCompletions = ObjectArrays.concat(modCompletions, currentCompletions, String.class);
        }
        return currentCompletions;
    }

    /**
     * @author hyperium
     */
    @Overwrite
    public void handleTimeUpdate(S03PacketTimeUpdate packet) {
        if (this.timeChanger == null) {
            this.timeChanger = Hyperium.INSTANCE.getModIntegration().getTimeChanger();
        }

        if (this.timeChanger.getTimeType() == null) {
            handleActualPacket(packet);
            return;
        }

        switch (this.timeChanger.getTimeType()) {
            case DAY:
                handleActualPacket(new S03PacketTimeUpdate(packet.getWorldTime(), -6000L, true));
                break;
            case SUNSET:
                handleActualPacket(new S03PacketTimeUpdate(packet.getWorldTime(), -22880L, true));
                break;
            case NIGHT:
                handleActualPacket(new S03PacketTimeUpdate(packet.getWorldTime(), -18000L, true));
                break;
            case VANILLA:
                handleActualPacket(packet);
                break;
        }
    }

    private void handleActualPacket(S03PacketTimeUpdate packetIn) {
        if (this.gameController == null || this.gameController.theWorld == null) {
            return;
        }

        PacketThreadUtil.checkThreadAndEnqueue(packetIn,
            (INetHandlerPlayClient) Minecraft.getMinecraft().getNetHandler().getNetworkManager().getNetHandler(), this.gameController);
        this.gameController.theWorld.setTotalWorldTime(packetIn.getTotalWorldTime());
        this.gameController.theWorld.setWorldTime(packetIn.getWorldTime());
    }

    /**
     * @author hyperium
     */
    @Overwrite
    public void handleAnimation(S0BPacketAnimation packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, (INetHandlerPlayClient) getNetworkManager().getNetHandler(), this.gameController);

        // Stops the code if the world is null, usually due to a weird packet from the server
        if (this.clientWorldController == null) {
            return;
        }

        Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityID());

        if (entity != null) {
            if (packetIn.getAnimationType() == 0) {
                EntityLivingBase entitylivingbase = (EntityLivingBase) entity;
                entitylivingbase.swingItem();
            } else if (packetIn.getAnimationType() == 1) {
                entity.performHurtAnimation();
            } else if (packetIn.getAnimationType() == 2) {
                EntityPlayer entityplayer = (EntityPlayer) entity;
                entityplayer.wakeUpPlayer(false, false, false);
            } else if (packetIn.getAnimationType() == 4) {
                this.gameController.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.CRIT);
            } else if (packetIn.getAnimationType() == 5) {
                this.gameController.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.CRIT_MAGIC);
            }
        }
    }

    @Inject(method = "handleResourcePack", at = @At("HEAD"), cancellable = true)
    private void handle(S48PacketResourcePackSend packetIn, CallbackInfo info) {
        if (!validateResourcePackUrl(packetIn.getURL(), packetIn.getHash())) info.cancel();
    }

    private boolean validateResourcePackUrl(String url, String hash) {
        try {
            URI uri = new URI(url);
            String scheme = uri.getScheme();
            boolean isLevelProtocol = "level".equals(scheme);
            if (!"http".equals(scheme) && !"https".equals(scheme) && !isLevelProtocol) {
                netManager.sendPacket(new C19PacketResourcePackStatus(hash, C19PacketResourcePackStatus.Action.FAILED_DOWNLOAD));
                throw new URISyntaxException(url, "Wrong protocol");
            }
            url = URLDecoder.decode(url.substring("level://".length()), StandardCharsets.UTF_8.toString());
            if (isLevelProtocol && (url.contains("..") || !url.endsWith("/resources.zip"))) {
                EntityPlayerSP thePlayer = Minecraft.getMinecraft().thePlayer;
                if (thePlayer != null) {
                    thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + EnumChatFormatting.BOLD.toString() + "[WARNING] The current server has tried to hack you, but HyperiumJailbreak stopped it."));
                }
                throw new URISyntaxException(url, "Invalid levelstorage resourcepack path");
            }
            return true;
        } catch (URISyntaxException e) {
            return false;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @author hyperium
     */
    @Overwrite
    public void handleChat(S02PacketChat packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, (INetHandlerPlayClient) getNetworkManager().getNetHandler(), this.gameController);

        ServerChatEvent event = new ServerChatEvent(packetIn.getType(), packetIn.getChatComponent());

        EventBus.INSTANCE.post(event);

        // If the event is cancelled or the message is empty, we'll ignore the packet.
        if (event.isCancelled() || event.getChat().getFormattedText().isEmpty()) {
            return;
        }

        if (packetIn.getType() == 2) {
            this.gameController.ingameGUI.setRecordPlaying(event.getChat(), false);
        } else {
            // This will then trigger the other chat event
            this.gameController.ingameGUI.getChatGUI().printChatMessage(event.getChat());
        }
    }
}
