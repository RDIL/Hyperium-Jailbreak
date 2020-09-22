package me.semx11.autotip.command;

import cc.hyperium.commands.BaseCommand;
import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;
import me.semx11.autotip.Autotip;
import me.semx11.autotip.chat.MessageUtil;

public class CommandLimbo implements BaseCommand {
    private Autotip autotip;
    private boolean executed;

    public CommandLimbo(Autotip autotip) {
        this.autotip = autotip;
    }

    public boolean hasExecuted() {
        return executed;
    }

    public void setExecuted(boolean executed) {
        this.executed = executed;
    }

    @Override
    public String getName() {
        return "limbo";
    }

    @Override
    public String getUsage() {
        return "/limbo";
    }

    @Override
    public void onExecute(String[] args) {
        MessageUtil messageUtil = autotip.getMessageUtil();

        if (autotip.getSessionManager().isOnHypixel()) {
            this.executed = true;
            messageUtil.sendCommand("/achat \u00a7c");
        } else {
            GeneralChatHandler.instance().sendMessage("You must be on Hypixel to use this command!");
        }
    }
}
