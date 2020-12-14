package com.hyperiumjailbreak;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.network.chat.ChatEvent;

public class ChatCanceller {
    private final String whatToListenFor;

    public ChatCanceller(String whatToListenFor) {
        this.whatToListenFor = whatToListenFor;
    }

    @InvokeEvent
    public void onChat(ChatEvent event) {
        if (event.getChat().getUnformattedText().contains(this.whatToListenFor)) {
            event.setCancelled(true);
        }
    }
}
