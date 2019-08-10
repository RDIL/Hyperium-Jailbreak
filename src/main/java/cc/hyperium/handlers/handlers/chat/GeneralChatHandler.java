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

package cc.hyperium.handlers.handlers.chat;
import cc.hyperium.Hyperium;
import cc.hyperium.event.ChatEvent;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.TickEvent;
import cc.hyperium.utils.ChatColor;
import cc.hyperium.utils.JsonHolder;
import cc.hyperium.config.Settings;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import java.io.InputStreamReader;
import java.util.EnumMap;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Pattern;

public class GeneralChatHandler {
    private static GeneralChatHandler instance = null;
    private final List<HyperiumChatHandler> handlerList;

    // Queued messages
    private final ConcurrentLinkedQueue<IChatComponent> messages = new ConcurrentLinkedQueue<>();

    private boolean posted = false;

    public GeneralChatHandler(List<HyperiumChatHandler> handlerList) {
        this.handlerList = handlerList;
        instance = this;
    }

    public void sendMessage(IChatComponent component) {
        if (component == null) component = new ChatComponentText("");
        this.messages.add(component);
    }

    public void sendMessage(String message, boolean addHeader) {
        if (message == null) return;

        if (addHeader) {
            if (Settings.HYPERIUM_CHAT_PREFIX) {
                message = ChatColor.RED + "[HyperiumJailbreak] " + ChatColor.WHITE.toString() + message;
            } else {
                message = ChatColor.WHITE.toString() + message;
            }
        }
        sendMessage(new ChatComponentText(message));
    }

    public void sendMessage(String message) {
        sendMessage(message, true);
    }

    @InvokeEvent
    public void tick(TickEvent event) {
        if (Minecraft.getMinecraft().thePlayer == null) {
            return;
        }

        while (!this.messages.isEmpty()) {
            Minecraft.getMinecraft().thePlayer.addChatComponentMessage(this.messages.poll());
        }
    }

    @InvokeEvent
    public void chatEvent(ChatEvent event) {
        boolean state = true;

        if (!this.posted) {
            return;
        }

        for (HyperiumChatHandler chatHandler : this.handlerList) {
            // Surround in try catch so errors don't stop further chat parsers
            try {
                // Is reversed because chathandlers weren't called if state was false, since
                // false && boolean will always be false, so it skipped the
                // HyperiumChatHandler#chatReceived method
                state = chatHandler.chatReceived(event.getChat(), strip(event.getChat())) && state;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (state) event.setCancelled(true);
    }

    public void post() {
        JsonHolder data = new JsonHolder(new JsonParser().parse(new InputStreamReader(GeneralChatHandler.class.getResourceAsStream("/remoteresources/chat_regex.json"))).getAsJsonObject());

        HyperiumChatHandler.regexPatterns = new EnumMap<>(HyperiumChatHandler.ChatRegexType.class);

        for (HyperiumChatHandler.ChatRegexType type : HyperiumChatHandler.ChatRegexType.values()) {
            if (!data.has(type.name().toLowerCase())) {
                Hyperium.LOGGER.error("Could not find chat regex type " + type.name().toLowerCase());
                continue;
            }

            HyperiumChatHandler.regexPatterns.put(type, Pattern.compile(data.optString(type.name().toLowerCase())));
        }

        this.posted = true;

        for (HyperiumChatHandler chatHandler : this.handlerList) {
            chatHandler.callback(data);
        }
    }

    public static String strip(IChatComponent component) {
        return ChatColor.stripColor(component.getUnformattedText());
    }

    public static GeneralChatHandler instance() {
        return instance;
    }
}
