package me.semx11.autotip.chat;

import com.google.common.collect.Queues;
import java.util.Queue;
import me.semx11.autotip.Autotip;
import me.semx11.autotip.universal.UniversalUtil;
import me.semx11.autotip.util.StringUtil;
import net.minecraft.client.Minecraft;

public class MessageUtil {
    private static final String PREFIX = "&6A&eT &8> &7";

    private final Autotip autotip;

    private final Queue<String> chatQueue = Queues.newConcurrentLinkedQueue();
    private final Queue<String> cmdQueue = Queues.newConcurrentLinkedQueue();

    public MessageUtil(Autotip autotip) {
        this.autotip = autotip;
    }

    public KeyHelper getKeyHelper(String rootKey) {
        return new KeyHelper(this, rootKey);
    }

    public void sendKey(String key, Object... params) {
        this.send(this.getKey(key), params);
    }

    public String getKey(String key) {
        return autotip.getLocaleHolder().getKey(key);
    }

    public void send(String msg, Object... params) {
        this.sendRaw(PREFIX + msg, params);
    }

    void separator() {
        this.sendRaw("&6&m&l----------------------------------");
    }

    private void sendRaw(String msg, Object... params) {
        msg = StringUtil.params(msg, params);
        if (this.isPlayerLoaded()) {
            this.flushQueues();
            UniversalUtil.addChatMessage(msg);
        } else {
            chatQueue.add(msg);
        }
    }

    public void sendCommand(String command) {
        if (this.isPlayerLoaded()) {
            this.flushQueues();
            autotip.getMinecraft().thePlayer.sendChatMessage(command);
        } else {
            cmdQueue.add(command);
        }
    }

    public void flushQueues() {
        if (this.isPlayerLoaded()) {
            while (!chatQueue.isEmpty()) {
                this.sendRaw(chatQueue.poll());
            }
            while (!cmdQueue.isEmpty()) {
                this.sendCommand(cmdQueue.poll());
            }
        }
    }

    public void clearQueues() {
        chatQueue.clear();
        cmdQueue.clear();
    }

    private boolean isPlayerLoaded() {
        Minecraft minecraft = autotip.getMinecraft();
        return minecraft != null && minecraft.thePlayer != null;
    }
}
