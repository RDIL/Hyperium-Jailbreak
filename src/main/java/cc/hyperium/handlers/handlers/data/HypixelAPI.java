package cc.hyperium.handlers.handlers.data;
import cc.hyperium.Hyperium;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.network.server.hypixel.JoinHypixelEvent;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.mods.sk1ercommon.Sk1erMod;
import cc.hyperium.utils.JsonHolder;
import cc.hyperium.utils.UUIDUtil;
import cc.hyperium.utils.Utils;
import com.google.gson.JsonElement;
import net.hypixel.api.HypixelApiFriends;
import net.hypixel.api.HypixelApiGuild;
import net.hypixel.api.HypixelApiPlayer;
import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class HypixelAPI {
    public static HypixelAPI INSTANCE;
    private final AsyncLoadingCache<String, HypixelApiPlayer> PLAYERS = Caffeine.newBuilder()
        .maximumSize(1_000)
        .expireAfterWrite(Duration.ofMinutes(5))
        .executor(Multithreading.POOL)
        .buildAsync(this::getApiPlayer);

    private final AsyncLoadingCache<String, HypixelApiFriends> FRIENDS = Caffeine.newBuilder()
        .maximumSize(1_000)
        .expireAfterWrite(Duration.ofMinutes(5))
        .executor(Multithreading.POOL)
        .buildAsync(this::getApiFriends);

    private final AsyncLoadingCache<String, HypixelApiGuild> GUILDS = Caffeine.newBuilder()
        .maximumSize(1_000)
        .expireAfterWrite(Duration.ofMinutes(5))
        .executor(Multithreading.POOL)
        .buildAsync(this::getApiGuild);

    private List<UUID> friendsForCurrentUser = new ArrayList<>();

    public HypixelAPI() {
        Multithreading.schedule(this::updatePersonalData, 10L, 305, TimeUnit.SECONDS);
        INSTANCE = this;
    }

    @InvokeEvent
    public void joinHypixel(JoinHypixelEvent event) {
        refreshCurrentUser();
        refreshFriendsForCurrentUser();
    }

    public CompletableFuture<HypixelApiPlayer> getPlayer(String key) {
        return PLAYERS.get(key);
    }

    public CompletableFuture<HypixelApiPlayer> getCurrentUser() {
        return getPlayer(UUIDUtil.getUUIDWithoutDashes());
    }

    public void refreshPlayer(String key) {
        PLAYERS.synchronous().refresh(key);
    }

    public void refreshCurrentUser() {
        refreshPlayer(getKeyForCurrentUser());
    }

    public CompletableFuture<HypixelApiFriends> getFriends(String key) {
        return FRIENDS.get(key);
    }

    public CompletableFuture<HypixelApiFriends> getFriendsForCurrentUser() {
        return getFriends(getKeyForCurrentUser()).whenComplete((data, error) -> {
            if (error != null) return;
            friendsForCurrentUser.clear();
            for (JsonElement friend : data.getFriends()) {
                friendsForCurrentUser.add(Utils.dashMeUp(new JsonHolder(friend.getAsJsonObject()).optString("uuid")));
            }
        });
    }

    public void refreshFriends(String key) {
        FRIENDS.synchronous().refresh(key);
    }

    public void refreshFriendsForCurrentUser() {
        refreshFriends(getKeyForCurrentUser());
    }

    public List<UUID> getListOfCurrentUsersFriends() {
        return friendsForCurrentUser;
    }

    public CompletableFuture<HypixelApiGuild> getGuildFromPlayer(String playerName) {
        return getGuild(GuildKey.fromPlayer(playerName));
    }

    public CompletableFuture<HypixelApiGuild> getGuild(GuildKey key) {
        return GUILDS.get(key.toString());
    }

    private void updatePersonalData() {
        if (Hyperium.INSTANCE.getHandlers().getHypixelDetector().isHypixel()) {
            refreshFriendsForCurrentUser();
            refreshCurrentUser();
        }
    }

    private String getKeyForCurrentUser() {
        return UUIDUtil.getUUIDWithoutDashes();
    }

    private HypixelApiFriends getApiFriends(String key) {
        return new HypixelApiFriends(new JsonHolder(Sk1erMod.getInstance().rawWithAgent("https://api.sk1er.club/friends/" + key.toLowerCase())));
    }

    private HypixelApiPlayer getApiPlayer(String key) {
        return new HypixelApiPlayer(new JsonHolder(Sk1erMod.getInstance().rawWithAgent("https://api.sk1er.club/player/" + key.toLowerCase())));
    }

    private HypixelApiGuild getApiGuild(String key) {
        GuildKey guildKey = GuildKey.fromSerialized(key);
        return new HypixelApiGuild(new JsonHolder(Sk1erMod.getInstance().rawWithAgent(String.format(guildKey.type.getUrl(), (Object) guildKey.formatStrings))));
    }

    enum GuildKeyType {
        PLAYER("https://api.sk1er.club/guild/player/%s"),
        NAME("https://api.sk1er.club/guild/name/");

        private final String url;
        GuildKeyType(String url) {
            this.url = url;
        }
        public String getUrl() {
            return url;
        }
    }

    public static class GuildKey {
        private final GuildKeyType type;
        private final String[] formatStrings;

        public GuildKey(GuildKeyType type, String... formatStrings) {
            this.type = type;
            this.formatStrings = formatStrings;
        }

        public static GuildKey fromSerialized(String serialized) {
            String type = serialized.split(";")[0];
            return new GuildKey(GuildKeyType.valueOf(type), serialized.split(";")[1].split(","));
        }

        public static GuildKey fromPlayer(String playerName) {
            return new GuildKey(GuildKeyType.PLAYER, playerName);
        }

        @Override
        public String toString() {
            return type.toString() + ";" + String.join(",", formatStrings);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj instanceof GuildKey) {
                GuildKey key = ((GuildKey) obj);
                return key.type == this.type && Arrays.equals(key.formatStrings, this.formatStrings);
            }
            return false;
        }
    }
}
