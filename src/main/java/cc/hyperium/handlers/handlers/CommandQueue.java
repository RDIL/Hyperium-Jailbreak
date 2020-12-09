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

package cc.hyperium.handlers.handlers;

import cc.hyperium.mods.sk1ercommon.Multithreading;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

/**
 * Hyperium's command queue.
 * This allows for commands in the queue to be executed one-by-one, one at a time (every second).
 * It also allows us to ensure that we don't send commands too fast,
 * which servers like Hypixel really don't enjoy.
 */
public class CommandQueue {
    private final Queue<String> commands = new ConcurrentLinkedQueue<>();
    private final Map<String, Runnable> asyncCallbacks = new ConcurrentHashMap<>();

    /**
     * Creates a new instance of the class, and schedules the execution every second of queued commands.
     */
    public CommandQueue() {
        Multithreading.schedule(CommandQueue.this::check, 0, 1000, TimeUnit.MILLISECONDS);
    }

    /**
     * Adds a command to the queue.
     *
     * @param chat The chat message to send.
     * @param task The callback to run after sending the command.
     */
    public void register(final String chat, final Runnable task) {
        asyncCallbacks.put(chat, task);
        queue(chat);
    }

    private void check() {
        if (!commands.isEmpty()) {
            EntityPlayerSP thePlayer = Minecraft.getMinecraft().thePlayer;
            if (thePlayer != null && commands.peek() != null) {
                String poll = commands.poll();
                Runnable runnable = asyncCallbacks.get(poll);
                thePlayer.sendChatMessage(poll);
                if (runnable != null) {
                    runnable.run();
                }
                asyncCallbacks.remove(poll);
            }
        }
    }

    /**
     * Does the same thing as {@link CommandQueue#register(String, Runnable)}, just without a {@link Runnable}.
     *
     * @param message The chat message/command to be queued.
     */
    public void queue(final String message) {
        commands.add(message);
    }
}
