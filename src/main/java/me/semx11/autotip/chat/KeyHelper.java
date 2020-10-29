package me.semx11.autotip.chat;

public class KeyHelper {
    private final MessageUtil messageUtil;
    private final String rootKey;

    KeyHelper(MessageUtil messageUtil, String rootKey) {
        this.messageUtil = messageUtil;
        this.rootKey = rootKey;
    }

    public KeyHelper separator() {
        messageUtil.separator();
        return this;
    }

    public KeyHelper sendKey(String key, Object... params) {
        messageUtil.sendKey(this.getAbsoluteKey(key), params);
        return this;
    }

    public String getKey(String key) {
        return messageUtil.getKey(this.getAbsoluteKey(key));
    }

    private String getAbsoluteKey(String relativeKey) {
        return rootKey + "." + relativeKey;
    }
}
