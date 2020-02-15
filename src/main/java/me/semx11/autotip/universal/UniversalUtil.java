package me.semx11.autotip.universal;

import cc.hyperium.event.network.chat.ServerChatEvent;
import me.semx11.autotip.Autotip;
import me.semx11.autotip.chat.ChatColor;
import me.semx11.autotip.util.MinecraftVersion;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class UniversalUtil {
    private static Autotip autotip;

    public static void setAutotip(Autotip autotip) {
        UniversalUtil.autotip = autotip;
    }

    public static MinecraftVersion getMinecraftVersion() {
        return MinecraftVersion.V1_8_9;
    }

    public static String getUnformattedText(ServerChatEvent event) {
        return EnumChatFormatting.getTextWithoutFormattingCodes(event.getChat().getUnformattedText());
    }

    public static String getUnformattedText(Object component) {
        if (component == null) {
            return null;
        }

        String unformattedText = ((IChatComponent) component).getUnformattedText();
        return ChatColor.stripFormatting(unformattedText);
    }

    public static String getHoverText(ServerChatEvent event) {
        return getHoverText(event.getChat());
    }

    private static String getHoverText(Object component) {
        IChatComponent chatComponent = (IChatComponent) component;

        if (component == null || chatComponent.getChatStyle() == null || chatComponent.getChatStyle().getChatHoverEvent() == null) {
            return null;
        }

        IChatComponent hoverText = chatComponent.getChatStyle().getChatHoverEvent().getValue();

        return getUnformattedText(hoverText);
    }

    public static void addChatMessage(String text) {
        addChatMessage(newComponent(text));
    }

    private static void addChatMessage(Object component) {
        EntityPlayerSP thePlayer = autotip.getMinecraft().thePlayer;
        thePlayer.addChatMessage((IChatComponent) component);
    }

    private static IChatComponent newComponent(String text) {
        return new ChatComponentText(text);
    }
}
