package cc.hyperium.handlers.handlers.data;
import cc.hyperium.Hyperium;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.JoinHypixelEvent;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.mods.sk1ercommon.Sk1erMod;
import cc.hyperium.netty.utils.Utils;
import cc.hyperium.utils.JsonHolder;
import cc.hyperium.utils.UUIDUtil;
import club.sk1er.website.api.requests.HypixelApiFriends;
import club.sk1er.website.api.requests.HypixelApiPlayer;
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
            data.getFriends().forEach(friend -> friendsForCurrentUser.add(Utils.dashMeUp(new JsonHolder(friend.getAsJsonObject()).optString("uuid"))));
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

    public CompletableFuture<JsonHolder> getLeaderboardWithID(String ID) {
        return CompletableFuture.supplyAsync(() -> new JsonHolder(Sk1erMod.getInstance().rawWithAgent("https://api.sk1er.club/leaderboard/" + ID)), Multithreading.POOL);
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
}
