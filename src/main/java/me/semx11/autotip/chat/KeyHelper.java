package me.semx11.autotip.chat;

public class KeyHelper {
    private final MessageUtil messageUtil;

    KeyHelper(MessageUtil messageUtil, String rootKey) {
        this.messageUtil = messageUtil;
    }

    public KeyHelper separator() {
        messageUtil.separator();
        return this;
    }
}
