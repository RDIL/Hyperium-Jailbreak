package com.hyperiumjailbreak;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.ChatEvent;

public class ChatCanceller {
    private String whatToListenFor;

    public ChatCanceller(String whatToListenFor) {
        this.whatToListenFor = whatToListenFor;
    }

    @InvokeEvent
    public void onChat(ChatEvent event) {
        if(event.getChat().getUnformattedText().contains(this.whatToListenFor)) event.setCancelled(true);
    }
}
