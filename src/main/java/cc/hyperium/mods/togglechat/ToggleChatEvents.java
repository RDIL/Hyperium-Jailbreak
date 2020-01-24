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

package cc.hyperium.mods.togglechat;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.Priority;
import cc.hyperium.event.network.chat.ServerChatEvent;
import cc.hyperium.mods.togglechat.toggles.ToggleBase;
import cc.hyperium.mods.togglechat.toggles.defaults.TypeMessageSeparator;
import cc.hyperium.utils.ChatColor;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;

public class ToggleChatEvents {
    private final ToggleChatMod mod;

    public ToggleChatEvents(ToggleChatMod theMod) {
        this.mod = theMod;
    }

    @InvokeEvent(priority = Priority.HIGH)
    public void onChatReceive(ServerChatEvent event) {
        String unformattedText = ChatColor.stripColor(event.getChat().getUnformattedText());
        String formattedText = event.getChat().getFormattedText();

        try {
            for (ToggleBase type : this.mod.getToggleHandler().getToggles().values()) {
                if (type.isEnabled()) continue;

                try {
                    String input = type.useFormattedMessage() ? formattedText : unformattedText;

                    if (type.shouldToggle(input)) {
                        if (type instanceof TypeMessageSeparator) {
                            ChatStyle style = event.getChat().getChatStyle();

                            String edited = ((TypeMessageSeparator) type).editMessage(formattedText);

                            if (!input.equals(edited) && edited.isEmpty()) {
                                event.setCancelled(true);
                            } else {
                                event.setChat(new ChatComponentText(edited).setChatStyle(style));
                            }
                        } else {
                            event.setCancelled(true);
                        }
                        break;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}
