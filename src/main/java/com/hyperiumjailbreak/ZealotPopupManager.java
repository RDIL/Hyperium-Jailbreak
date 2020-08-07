package com.hyperiumjailbreak;

import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.network.chat.ChatEvent;

public class ZealotPopupManager {
    public ZealotPopupManager() {
        EventBus.INSTANCE.register(this);
    }

    @InvokeEvent
    public void onChat(ChatEvent c) {
        if (c.getChat().getUnformattedText().contains("A special Zealot has spawned")) {
            new Popup("Special Zealot!", "Look around, there is a summoning eye!").startShowing();
        }
    }
}
