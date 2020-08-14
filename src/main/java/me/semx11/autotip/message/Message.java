package me.semx11.autotip.message;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import me.semx11.autotip.chat.MessageOption;
import me.semx11.autotip.gson.exclusion.Exclude;

public class Message {
    @Exclude
    private Map<String, MessageMatcher> messageCache = new ConcurrentHashMap<>();

    protected Pattern pattern;
    private MessageOption hideFor;

    Message(Pattern pattern) {
        this(pattern, MessageOption.HIDDEN);
    }

    Message(Pattern pattern, MessageOption hideFor) {
        this.pattern = pattern;
        this.hideFor = hideFor;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public MessageMatcher getMatcherFor(String input) {
        if (messageCache == null) messageCache = new ConcurrentHashMap<>();
        if (messageCache.containsKey(input)) {
            return messageCache.get(input);
        }
        MessageMatcher matcher = new MessageMatcher(pattern, input);
        messageCache.put(input, matcher);
        return matcher;
    }

    public boolean shouldHide(MessageOption option) {
        return hideFor.compareTo(option) <= 0;
    }
}
