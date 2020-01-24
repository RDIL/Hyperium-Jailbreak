package cc.hyperium.event.network.chat;

import cc.hyperium.event.CancellableEvent;

public class SendChatMessageEvent extends CancellableEvent {
    private final String message;
    public SendChatMessageEvent(String message) {
        this.message = message;
    }
    public String getMessage() {
        return this.message;
    }
}
