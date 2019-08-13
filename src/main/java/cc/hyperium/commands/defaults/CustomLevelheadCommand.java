package cc.hyperium.commands.defaults;

import cc.hyperium.commands.BaseCommand;
import cc.hyperium.gui.CustomLevelheadConfigurer;

public class CustomLevelheadCommand implements BaseCommand {
    @Override
    public String getName() {
        return "customlevelhead";
    }

    @Override
    public String getUsage() {
        return "/" + getName();
    }

    @Override
    public void onExecute(String[] args) {
        new CustomLevelheadConfigurer().show();
    }
}
