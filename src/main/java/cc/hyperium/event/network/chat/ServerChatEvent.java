package cc.hyperium.event.network.chat;

import net.minecraft.util.IChatComponent;
import cc.hyperium.event.CancellableEvent;

public class ServerChatEvent extends CancellableEvent {
    private final byte type;
    private IChatComponent chat;
    public ServerChatEvent(byte type, IChatComponent chat) {
        this.type = type;
        this.chat = chat;
    }
    public byte getType() {
        return this.type;
    }
    public IChatComponent getChat() {
        return this.chat;
    }
    public void setChat(IChatComponent chat) {
        this.chat = chat;
    }
}
