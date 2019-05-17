package cc.hyperium.commands.defaults;

import cc.hyperium.commands.BaseCommand;
import java.io.IOException;

public class Stacktest implements BaseCommand {
    @Override
    public String getName() {
        return "stacktrace";
    }

    @Override
    public String getUsage() {
        return "/stacktrace";
    }

    @Override
    public void onExecute(String[] args) throws Exception {
        throw new IOException("This is a test");
    }
}
