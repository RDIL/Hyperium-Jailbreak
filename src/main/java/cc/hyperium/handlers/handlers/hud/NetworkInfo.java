package cc.hyperium.handlers.handlers.hud;

import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.EnumChatFormatting;
import java.util.Collection;

public class NetworkInfo {
    private static NetworkInfo instance;
    private Minecraft mc;

    public NetworkInfo() {
        this.mc = Minecraft.getMinecraft();
        NetworkInfo.instance = this;
    }

    public static NetworkInfo getInstance() {
        return NetworkInfo.instance;
    }

    private NetworkPlayerInfo getPlayerInfo(final String ign) {
        final Collection<NetworkPlayerInfo> map = this.mc.getNetHandler().getPlayerInfoMap();
        for (final NetworkPlayerInfo networkplayerinfo : map) {
            if (networkplayerinfo.getGameProfile().getName().equalsIgnoreCase(ign)) {
                return networkplayerinfo;
            }
        }
        return null;
    }

    public void printPing(final String name) {
        final NetworkPlayerInfo info = this.getPlayerInfo(name);
        if (info == null || info.getResponseTime() < 0) {
            GeneralChatHandler.instance().sendMessage(EnumChatFormatting.RED + "No info about " + name);
        } else {
            GeneralChatHandler.instance().sendMessage(EnumChatFormatting.WHITE.toString() + EnumChatFormatting.BOLD + info.getGameProfile().getName() + EnumChatFormatting.WHITE + ": " + info.getResponseTime() + "ms");
        }
    }
}
