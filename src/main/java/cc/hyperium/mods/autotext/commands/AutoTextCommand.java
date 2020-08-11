package cc.hyperium.mods.autotext.commands;

import cc.hyperium.Hyperium;
import cc.hyperium.commands.BaseCommand;
import cc.hyperium.mods.autotext.gui.SetBindGui;

public class AutoTextCommand implements BaseCommand {
    @Override
    public String getName() {
        return "setbind";
    }

    @Override
    public String getUsage() {
        return "/" + getName();
    }

    @Override
    public void onExecute(String[] args) {
        Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new SetBindGui());
    }
}
