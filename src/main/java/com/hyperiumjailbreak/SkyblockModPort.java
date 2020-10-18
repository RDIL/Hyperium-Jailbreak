package com.hyperiumjailbreak;

import cc.hyperium.utils.ChatColor;
import com.google.common.collect.Sets;
import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import java.util.Set;

/**
 * Skyblock addons port (only some parts)
 * @author biscuut
 * @implNote source: https://github.com/biscuut/skyblockaddons
 */
public class SkyblockModPort {
    private static final Set<String> skyblockInAllLanguages = Sets.newHashSet("SKYBLOCK", "\u7A7A\u5C9B\u751F\u5B58");

    public static boolean playing() {
        final Scoreboard s = Minecraft.getMinecraft().theWorld.getScoreboard();
        ScoreObjective so = s.getObjectiveInDisplaySlot(1);
        if (so != null) {
            String objectiveName = ChatColor.stripColor(so.getDisplayName());
            for (String skyblock : skyblockInAllLanguages) {
                if (objectiveName.startsWith(skyblock)) {
                    return true;
                }
            }
        }
        return false;
    }
}
