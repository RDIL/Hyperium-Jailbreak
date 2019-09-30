package cc.hyperium.handlers.handlers;

import cc.hyperium.Hyperium;
import cc.hyperium.config.Settings;
import cc.hyperium.event.AchievementGetEvent;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.ServerChatEvent;
import me.lpk.util.StringUtils;
import net.minecraft.util.EnumChatFormatting;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BroadcastEvents {
    private final Pattern ACHIEVEMENT_PATTERN = Pattern.compile("a>> {3}Achievement Unlocked: (?<achievement>.+) {3}<<a");

    @InvokeEvent
    public void acheivementGet(AchievementGetEvent event) {
        if (Settings.BROADCAST_ACHIEVEMENTS) {
            Hyperium.INSTANCE.getHandlers().getCommandQueue().queue("/gchat Achievement unlocked! I unlocked the " + event.getAchievement() + " achievement");
        }
    }

    @InvokeEvent
    public void checkForEvents(ServerChatEvent event) {
        String raw = EnumChatFormatting.getTextWithoutFormattingCodes(event.getChat().getUnformattedText());
        Matcher achMatcher = ACHIEVEMENT_PATTERN.matcher(raw);
        if (achMatcher.matches()) {
            String ach = achMatcher.group("achievement");
            //Check to stop spamming of gchat if achievement is broken and you get it many times.
            if (!achievementsGotten.contains(ach)) {
                EventBus.INSTANCE.post(new AchievementGetEvent(ach));
                achievementsGotten.add(ach);
            }
        }
    }

    private List<String> achievementsGotten = new ArrayList<>();
}
