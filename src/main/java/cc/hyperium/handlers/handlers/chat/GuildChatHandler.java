package cc.hyperium.handlers.handlers.chat;

import cc.hyperium.config.Settings;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IChatComponent;

public class GuildChatHandler extends HyperiumChatHandler {
    @Override
    public boolean chatReceived(IChatComponent component, String text) {
        if (text.endsWith(" joined the guild!") && Settings.SEND_GUILD_WELCOME_MESSAGE) {
            int rankHeader = 0;
            if (text.contains("[")) rankHeader = text.indexOf("]") + 1;
            String playerName = String.valueOf(text.subSequence(rankHeader, text.length() - " joined the guild!".length())).trim();
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/gc Welcome to the guild " + playerName + "!");
        }
        return false;
    }
}
