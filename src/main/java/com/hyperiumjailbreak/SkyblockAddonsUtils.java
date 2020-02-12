package com.hyperiumjailbreak;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import cc.hyperium.event.EventBus;
import cc.hyperium.event.client.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.*;
import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SkyblockAddonsUtils {
    public SkyblockAddonsUtils() {
        EventBus.INSTANCE.register(this);
    }

    private final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)§[0-9A-FK-OR]");
    private static boolean onSkyblock = false;
    private SkyblockAddonsData.Location location = null;
    private int timerTick = 1;
    private static final Pattern SERVER_REGEX = Pattern.compile("([0-9]{2}/[0-9]{2}/[0-9]{2}) (mini[0-9]{1,3}[A-Za-z])");

    public void checkGameLocationDate() {
        boolean foundLocation = false;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc != null && mc.theWorld != null) {
            Scoreboard scoreboard = mc.theWorld.getScoreboard();
            ScoreObjective sidebarObjective = mc.theWorld.getScoreboard().getObjectiveInDisplaySlot(1);
            if (sidebarObjective != null) {
                Collection<Score> collection = scoreboard.getSortedScores(sidebarObjective);
                List<Score> list = Lists.newArrayList(collection.stream().filter(p_apply_1_ -> p_apply_1_.getPlayerName() != null && !p_apply_1_.getPlayerName().startsWith("#")).collect(Collectors.toList()));
                if (list.size() > 15) {
                    collection = Lists.newArrayList(Iterables.skip(list, collection.size() - 15));
                } else {
                    collection = list;
                }
                for (Score score1 : collection) {
                    ScorePlayerTeam scorePlayerTeam = scoreboard.getPlayersTeam(score1.getPlayerName());
                    String locationString = keepLettersAndNumbersOnly(
                            stripColor(ScorePlayerTeam.formatPlayerName(scorePlayerTeam, score1.getPlayerName())));
                    if (locationString.contains("mini")) {
                        Matcher matcher = SERVER_REGEX.matcher(locationString);
                        if (matcher.matches()) {
                            continue; // skip to next line
                        }
                    }
                    for (SkyblockAddonsData.Location loopLocation : SkyblockAddonsData.Location.values()) {
                        if (locationString.endsWith(loopLocation.getScoreboardName())) {
                            location = loopLocation;
                            foundLocation = true;
                            break;
                        }
                    }
                }
            }
        }
        if (!foundLocation) {
            location = null;
        }
    }

    private static final Pattern LETTERS_NUMBERS = Pattern.compile("[^a-z A-Z:0-9/']");

    private String keepLettersAndNumbersOnly(String text) {
        return LETTERS_NUMBERS.matcher(text).replaceAll("");
    }

    public boolean isNotNPC(Entity entity) {
        if (entity instanceof EntityOtherPlayerMP) {
            EntityPlayer p = (EntityPlayer)entity;
            Team team = p.getTeam();
            if (team instanceof ScorePlayerTeam) {
                ScorePlayerTeam playerTeam = (ScorePlayerTeam)team;
                String color = playerTeam.getColorPrefix();
                return color == null || !color.equals("");
            }
        }
        return true;
    }

    public int getDefaultColor(float alphaFloat) {
        int alpha = (int) alphaFloat;
        return new Color(150, 236, 255, alpha).getRGB();
    }

    public String stripColor(final String input) {
        return STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
    }

    public SkyblockAddonsData.Location getLocation() {
        return location;
    }

    public boolean isOnSkyblock() {
        return onSkyblock;
    }

    public void onTick(TickEvent e) {
        timerTick++;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc != null) {
            if (timerTick % 5 == 0) {
                EntityPlayerSP p = mc.thePlayer;
                if (p != null) {
                    checkGameLocationDate();
                }
            } else if (timerTick > 20) {
                timerTick = 1;
            }
        }
    }
}
