package cc.hyperium.commands.defaults;

import cc.hyperium.Hyperium;
import cc.hyperium.commands.BaseCommand;
import cc.hyperium.handlers.handlers.hud.TabCompletionUtil;
import net.minecraft.client.Minecraft;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandParty implements BaseCommand {
    @Override
    public String getName() {
        return "party";
    }

    @Override
    public String getUsage() {
        return "/party <command>";
    }

    @Override
    public List<String> getCommandAliases() {
        return Collections.singletonList("p");
    }

    @Override
    public void onExecute(String[] args) {
        StringBuilder builder = new StringBuilder();
        for (String arg : args) {
            builder.append(arg).append(" ");
        }
        Hyperium.INSTANCE.getHandlers().getCommandQueue().queue("/party" + builder.toString());
    }

    @Override
    public boolean tabOnly() {
        return true;
    }

    @Override
    public List<String> onTabComplete(String[] args) {
        if (!Hyperium.INSTANCE.getHandlers().getHypixelDetector().isHypixel()) {
            return new ArrayList<>();
        }

        final List<String> first = Arrays.asList("invite", "leave", "promote", "home", "remove", "warp", "accept", "disband", "settings", "mute", "poll", "challenge", "kickoffline", "private");
        final List<String> tabUsernames = TabCompletionUtil.getTabUsernames();

        try {
            for (String s : Hyperium.INSTANCE.getHandlers().getDataHandler().getFriendsForCurrentUser().get().getData().getKeys()) {
                String name = Hyperium.INSTANCE.getHandlers().getDataHandler().getFriendsForCurrentUser().get().getData().optJSONObject(s).optString("name");
                if (!name.isEmpty()) {
                    tabUsernames.add(name);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        final List<String> complete = new ArrayList<>(first);
        tabUsernames.remove(Minecraft.getMinecraft().getSession().getUsername());
        complete.addAll(tabUsernames);
        tabUsernames.clear();

        return TabCompletionUtil.getListOfStringsMatchingLastWord(args, complete);
    }
}
