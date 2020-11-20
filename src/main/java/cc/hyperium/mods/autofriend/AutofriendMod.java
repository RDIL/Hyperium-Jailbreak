package cc.hyperium.mods.autofriend;

import cc.hyperium.Hyperium;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.network.server.hypixel.HypixelFriendRequestEvent;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.mods.AbstractMod;
import net.minecraft.client.Minecraft;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class AutofriendMod extends AbstractMod {
    private static final Minecraft mc = Minecraft.getMinecraft();
    public static List<String> blacklist = new ArrayList<>();

    @Override
    public AbstractMod init() {
        Hyperium.CONFIG.register(AutoFriendOptionsProvider.INSTANCE);
        EventBus.INSTANCE.register(this);
        try {
            getBlacklist();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    @InvokeEvent
    public void friendRequestEvent(final HypixelFriendRequestEvent event) {
        String name = event.getFrom();
        if (AutoFriendOptionsProvider.AUTOFRIEND_TOGGLE && blacklist.stream().noneMatch(name::equalsIgnoreCase)) {
            mc.thePlayer.sendChatMessage("/friend accept " + name);
        }
    }

    private static void getBlacklist() throws IOException {
        final File blacklistFile = new File("config/autofriend.cfg");
        if (blacklistFile.exists()) {
            blacklist = Files.readAllLines(Paths.get(blacklistFile.toURI()));
            if (blacklist.get(0).equals("true") || blacklist.get(0).equals("false")) {
                AutoFriendOptionsProvider.AUTOFRIEND_MESSAGES = Boolean.parseBoolean(blacklist.get(0));
                blacklist.remove(0);
            } else {
                writeBlacklist();
            }
        } else {
            writeBlacklist();
        }
    }

    public static void writeBlacklist() {
        try {
            final File blacklistFile = new File("config/autofriend.cfg");

            if (!blacklistFile.getParentFile().exists() && !blacklistFile.getParentFile().mkdirs()) {
                return;
            }

            if (!blacklistFile.exists() && !blacklistFile.createNewFile()) {
                return;
            }

            final FileWriter writer = new FileWriter(blacklistFile);
            writer.write(Boolean.toString(AutoFriendOptionsProvider.AUTOFRIEND_MESSAGES));
            for (final String str : blacklist) {
                writer.write(System.lineSeparator() + str);
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
