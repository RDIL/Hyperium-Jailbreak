package com.hyperiumjailbreak;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.client.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.*;
import java.util.*;

public class SkyblockAddonsUtils {
    public SkyblockAddonsUtils() {
        EventBus.INSTANCE.register(this);
    }

    private SkyblockAddonsData.Location location = null;
    private int timerTick = 1;

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

    public SkyblockAddonsData.Location getLocation() {
        return location;
    }

    @InvokeEvent
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
