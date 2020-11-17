/*
 *     Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.mods.levelhead;

import cc.hyperium.Hyperium;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.client.TickEvent;
import cc.hyperium.mods.AbstractMod;
import cc.hyperium.mods.levelhead.commands.LevelHeadCommand;
import cc.hyperium.mods.levelhead.config.LevelheadConfig;
import cc.hyperium.mods.levelhead.renderer.LevelHeadRender;
import cc.hyperium.mods.levelhead.renderer.LevelheadTag;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.mods.sk1ercommon.Sk1erMod;
import cc.hyperium.utils.JsonHolder;
import cc.hyperium.utils.UUIDUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.scoreboard.Team;
import rocks.rdil.simpleconfig.Option;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class Levelhead extends AbstractMod {
    private static final String VERSION = "5.0";
    public final Map<UUID, LevelheadTag> levelCache = new HashMap<>();
    private final Map<UUID, Integer> timeCheck = new HashMap<>();
    public UUID userUuid = null;
    public int count = 1;
    public int wait = 60;
    @Option
    private String type = "LEVEL";
    private HashMap<UUID, String> trueValueCache = new HashMap<>();
    private Set<UUID> existedMorethan5Seconds = new HashSet<>();
    private long waitUntil = System.currentTimeMillis();
    private int updates = 0;
    private JsonHolder types = new JsonHolder();

    public Levelhead() {}

    public int getRGBColor() {
        return Color.HSBtoRGB(System.currentTimeMillis() % 1000L / 1000.0f, 0.8f, 0.8f);
    }

    public int getRGBDarkColor() {
        return Color.HSBtoRGB(System.currentTimeMillis() % 1000L / 1000.0f, 0.8f, 0.2f);
    }

    public AbstractMod init() {
        Multithreading.runAsync(() -> types = new JsonHolder(new Sk1erMod().rawWithAgent("https://backend.rdil.rocks/levelhead_config_mirror.json")));
        Hyperium.CONFIG.register(LevelheadConfig.INSTANCE);
        register(this);
        userUuid = UUIDUtil.getClientUUID();
        register(new LevelHeadRender(this), this);
        Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().registerCommand(new LevelHeadCommand(this));
        return this;
    }

    @SuppressWarnings("SimplifiableIfStatement")
    public boolean loadOrRender(EntityPlayer player) {
        if (!Hyperium.INSTANCE.getHandlers().getHypixelDetector().isHypixel()) return false;
        if (!LevelheadConfig.ENABLED) return false;

        for (PotionEffect effect : player.getActivePotionEffects()) {
            if (effect.getPotionID() == 14) return false;
        }
        if (!renderFromTeam(player)) return false;
        if (player.riddenByEntity != null) return false;
        int min = Math.min(64 * 64, LevelheadConfig.RENDER_DISTANCE * LevelheadConfig.RENDER_DISTANCE);
        if (player.getDistanceSqToEntity(Minecraft.getMinecraft().thePlayer) > min) return false;
        if (!this.existedMorethan5Seconds.contains(player.getUniqueID())) return false;

        if (player.hasCustomName() && player.getCustomNameTag().isEmpty()) return false;
        if (player.isInvisible() || player.isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer)) return false;
        if (player.isSneaking()) return false;
        return player.getAlwaysRenderNameTagForRender() && !player.getName().isEmpty();
    }

    private boolean renderFromTeam(EntityPlayer player) {
        Team team = player.getTeam();
        Team team1 = Minecraft.getMinecraft().thePlayer.getTeam();

        if (team != null) {
            Team.EnumVisible enumVisible = team.getNameTagVisibility();
            switch (enumVisible) {
                case NEVER:
                    return false;
                case HIDE_FOR_OTHER_TEAMS:
                    return team1 == null || team.isSameTeam(team1);
                case HIDE_FOR_OWN_TEAM:
                    return team1 == null || !team.isSameTeam(team1);
                default:
                    return true;
            }
        }
        return true;
    }

    @InvokeEvent
    public void tick(TickEvent event) {
        if (!Hyperium.INSTANCE.getHandlers().getHypixelDetector().isHypixel() || !LevelheadConfig.ENABLED) return;
        Minecraft mc = Minecraft.getMinecraft();

        if (!mc.isGamePaused() && mc.thePlayer != null && mc.theWorld != null) {
            if (System.currentTimeMillis() < this.waitUntil) {
                if (this.updates > 0)  this.updates = 0;
                return;
            }

            for (EntityPlayer entityPlayer : mc.theWorld.playerEntities) {
                if (!existedMorethan5Seconds.contains(entityPlayer.getUniqueID())) {
                    if (!timeCheck.containsKey(entityPlayer.getUniqueID())) timeCheck.put(entityPlayer.getUniqueID(), 0);
                    int old = timeCheck.get(entityPlayer.getUniqueID());
                    if (old > 100) {
                        existedMorethan5Seconds.add(entityPlayer.getUniqueID());
                    } else if (!entityPlayer.isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer))
                        timeCheck.put(entityPlayer.getUniqueID(), old + 1);
                }

                if (loadOrRender(entityPlayer)) {
                    final UUID uuid = entityPlayer.getUniqueID();
                    if (!levelCache.containsKey(uuid)) getLevel(uuid);
                }
            }
        }
    }

    private String trimUuid(UUID uuid) {
        return uuid.toString().replace("-", "");
    }

    private void getLevel(final UUID uuid) {
        if (updates >= count) {
            waitUntil = System.currentTimeMillis() + 1000 * wait;
            updates = 0;
            return;
        }
        updates++;
        levelCache.put(uuid, null);
        Multithreading.runAsync(() -> {
            String raw = new Sk1erMod().rawWithAgent(
                "https://api.sk1er.club/levelheadv5/" + trimUuid(uuid) + "/" + type
                    + "/" + trimUuid(Minecraft.getMinecraft().getSession().getProfile().getId()) +
                    "/" + VERSION);
            JsonHolder object = new JsonHolder(raw);
            if (!object.optBoolean("success")) {
                object.put("strlevel", "Error");
            }
            LevelheadTag value = buildTag(object);
            levelCache.put(uuid, value);
            trueValueCache.put(uuid, object.optString("strlevel"));
        });
        Multithreading.POOL.submit(this::clearCache);
    }

    public LevelheadTag buildTag(JsonHolder object) {
        LevelheadTag value = new LevelheadTag();
        JsonHolder headerObj = new JsonHolder();
        JsonHolder footerObj = new JsonHolder();
        JsonHolder construct = new JsonHolder();
        if (object.has("header_obj")) {
            headerObj = object.optJSONObject("header_obj");
            headerObj.put("custom", true);
        }
        if (object.has("footer_obj")) {
            footerObj = object.optJSONObject("footer_obj");
            footerObj.put("custom", true);
        }
        if (object.has("header")) {
            headerObj.put("header", object.optString("header"));
            headerObj.put("custom", true);
        }
        try {
            if (object.optInt("level") != Integer.parseInt(object.optString("strlevel"))) {
                footerObj.put("custom", true);
            }
        } catch (Exception ignored) {
            footerObj.put("custom", true);
        }
        headerObj.merge(getHeaderConfig(), false);
        footerObj.merge(getFooterConfig().put("footer", object.optString("strlevel", object.optInt("level") + "")), false);

        construct.put("header", headerObj).put("footer", footerObj);
        value.construct(construct);
        return value;
    }

    public JsonHolder getHeaderConfig() {
        JsonHolder holder = new JsonHolder();
        holder.put("chroma", LevelheadConfig.headerChroma);
        holder.put("rgb", LevelheadConfig.headerRgb);
        holder.put("red", LevelheadConfig.headerRed);
        holder.put("green", LevelheadConfig.headerGreen);
        holder.put("blue", LevelheadConfig.headerBlue);
        holder.put("color", LevelheadConfig.headerColor);
        holder.put("alpha", LevelheadConfig.headerAlpha);
        holder.put("header", LevelheadConfig.customHeader + ": ");
        return holder;
    }

    public JsonHolder getFooterConfig() {
        JsonHolder holder = new JsonHolder();
        holder.put("chroma", LevelheadConfig.footerChroma);
        holder.put("rgb", LevelheadConfig.footerRgb);
        holder.put("color", LevelheadConfig.footerColor);
        holder.put("red", LevelheadConfig.footerRed);
        holder.put("green", LevelheadConfig.footerGreen);
        holder.put("blue", LevelheadConfig.footerBlue);
        holder.put("alpha", LevelheadConfig.footerAlpha);
        return holder;
    }

    public String getType() {
        return type;
    }

    public void setType(String s) {
        this.type = s;
    }

    public JsonHolder getTypes() {
        return types;
    }

    public LevelheadTag getLevelString(UUID uuid) {
        return levelCache.getOrDefault(uuid, null);
    }

    private void clearCache() {
        if (levelCache.size() > Math.max(LevelheadConfig.PURGE_SIZE, 150)) {
            List<UUID> safePlayers = new ArrayList<>();
            for (EntityPlayer player : Minecraft.getMinecraft().theWorld.playerEntities) {
                if (existedMorethan5Seconds.contains(player.getUniqueID())) {
                    safePlayers.add(player.getUniqueID());
                }
            }
            existedMorethan5Seconds.clear();
            existedMorethan5Seconds.addAll(safePlayers);

            for (UUID uuid : levelCache.keySet()) {
                if (!safePlayers.contains(uuid)) {
                    levelCache.remove(uuid);
                }
            }
        }
    }

    private void register(Object... events) {
        for (Object o : events) {
            EventBus.INSTANCE.register(o);
        }
    }

    public HashMap<UUID, String> getTrueValueCache() {
        return trueValueCache;
    }
}
