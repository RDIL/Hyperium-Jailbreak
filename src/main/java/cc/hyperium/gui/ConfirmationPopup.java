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

package cc.hyperium.gui;

import cc.hyperium.Hyperium;
import cc.hyperium.config.Settings;
import cc.hyperium.event.network.server.hypixel.HypixelFriendRequestEvent;
import cc.hyperium.event.network.server.hypixel.HypixelPartyInviteEvent;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.interact.KeypressEvent;
import cc.hyperium.event.render.RenderHUDEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;
import java.awt.Color;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Consumer;

/**
 * The confirmation popup is an on-screen popup that allows you to easily accept friend and party requests.
 * For a more dulled-down version that only displays information, see {@link com.hyperiumjailbreak.Popup}.
 *
 * @see com.hyperiumjailbreak.Popup
 * @see Hyperium#getConfirmation()
 */
public class ConfirmationPopup {
    private final Queue<Confirmation> confirmations = new LinkedList<>();
    private Confirmation currentConfirmation;
    private String acceptFrom = "";

    @InvokeEvent
    public void onFriend(HypixelFriendRequestEvent e) {
        if (Settings.SHOW_INGAME_CONFIRMATION_POPUP) {
            displayConfirmation("Friend request from " + e.getFrom(), accept -> {
                Minecraft.getMinecraft().thePlayer.sendChatMessage((accept ? "/friend accept " : "/friend deny ") + e.getFrom());
                currentConfirmation.framesLeft = 0;
            }, 5);
        }
    }

    @InvokeEvent
    public void onParty(HypixelPartyInviteEvent e) {
        if (Settings.SHOW_INGAME_CONFIRMATION_POPUP) {
            displayConfirmation("Party request from " + e.getFrom(), accept -> {
                if (accept) {
                    Minecraft.getMinecraft().thePlayer.sendChatMessage("/party accept " + e.getFrom());
                }

                currentConfirmation.framesLeft = 0;
            }, 5);
        }
    }

    @InvokeEvent
    public void onRenderTick(RenderHUDEvent e) {
        if (currentConfirmation == null) {
            currentConfirmation = confirmations.poll();
            return;
        }

        if (currentConfirmation.render()) {
            currentConfirmation = confirmations.poll();
        }
    }

    @InvokeEvent
    public void onKeypress(KeypressEvent e) {
        if (currentConfirmation == null || Minecraft.getMinecraft().currentScreen != null) {
            return;
        }

        if (e.getKey() == Keyboard.KEY_Y) {
            currentConfirmation.callback.accept(true);
        } else if (e.getKey() == Keyboard.KEY_N) {
            currentConfirmation.callback.accept(false);
        }
    }

    /**
     * Queues a confirmation to be displayed on screen.
     *
     * @param text The popup's text.
     * @param callback The key-press callback. The value will be {@code true} if the user pressed {@code Y}, or @{code false} if they pressed {@code N}.
     * @param seconds The number of seconds to show the popup for.
     * @return The confirmation instance.
     */
    public Confirmation displayConfirmation(String text, Consumer<Boolean> callback, int seconds) {
        Confirmation c = new Confirmation(seconds * 60, seconds * 60, text, callback);
        if (Settings.SHOW_INGAME_CONFIRMATION_POPUP) {
            confirmations.add(c);
        }
        return c;
    }

    public void setAcceptFrom(String acceptFrom) {
        this.acceptFrom = acceptFrom;
    }

    /**
     * An actual confirmation.
     */
    public final class Confirmation {
        private final String text;
        private final Consumer<Boolean> callback;
        private final long upperThreshold;
        private final long lowerThreshold;
        private long framesLeft;
        private float percentComplete;
        private long systemTime;

        public Confirmation(long framesLeft, long frames, String text, Consumer<Boolean> callback) {
            this.framesLeft = framesLeft;
            this.text = text;
            this.callback = callback;

            long fifth = frames / 5;
            upperThreshold = frames - fifth;
            lowerThreshold = fifth;
            this.percentComplete = 0.0f;
            this.systemTime = Minecraft.getSystemTime();
        }

        /**
         * Renders the confirmation on screen.
         *
         * @return If the popup is done showing or should not be shown any more.
         */
        public boolean render() {
            if (framesLeft <= 0) {
                return true;
            }

            if (text.equalsIgnoreCase("Party request from " + acceptFrom)) {
                callback.accept(true);
                return true;
            }

            while (this.systemTime < Minecraft.getSystemTime() + (1000 / 60)) {
                this.framesLeft--;
                this.systemTime += (1000 / 60);
            }

            this.percentComplete = HyperiumGui.clamp(HyperiumGui.easeOut(this.percentComplete, this.framesLeft < lowerThreshold ? 0.0f : this.framesLeft > upperThreshold ? 1.0f : framesLeft, 0.01f, 15f), 0.0f, 1.0f);

            FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
            ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

            int middle = sr.getScaledWidth() / 2;
            int halfWidth = 105;
            int currWidth = (int) (halfWidth * percentComplete);

            // Background
            Gui.drawRect(middle - currWidth, 50, middle + currWidth, 95, new Color(30, 30, 30).getRGB());

            if (this.percentComplete == 1.0F) {
                long length = upperThreshold - lowerThreshold;
                long current = framesLeft - lowerThreshold;
                float progress = 1.0F - HyperiumGui.clamp((float) current / (float) length, 0.0F, 1.0F);

                // Progress
                Gui.drawRect(middle - currWidth, 93, (int) (middle - currWidth + (210 * progress)), 95, new Color(149, 201, 144).getRGB());

                fr.drawString(text, sr.getScaledWidth() / 2 - fr.getStringWidth(text) / 2, 58, 0xFFFFFF);

                String s = "[Y] Accept [N] Deny";
                fr.drawString(s, sr.getScaledWidth() / 2 - fr.getStringWidth(s) / 2, 70, new Color(170, 170, 170).getRGB());
            }

            return false;
        }
    }
}
