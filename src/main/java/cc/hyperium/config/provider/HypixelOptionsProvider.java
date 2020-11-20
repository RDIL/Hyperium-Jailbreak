package cc.hyperium.config.provider;

import cc.hyperium.config.ToggleSetting;
import rocks.rdil.simpleconfig.Option;

public class HypixelOptionsProvider implements IOptionSetProvider {
    public static final HypixelOptionsProvider INSTANCE = new HypixelOptionsProvider();

    @Override
    public String getName() {
        return "Hypixel";
    }
    
    @Option @ToggleSetting(name = "Thank Watchdog")
    public static boolean THANK_WATCHDOG = false;

    @Option @ToggleSetting(name = "Broadcast Achivements to Guild Chat")
    public static boolean BROADCAST_ACHIEVEMENTS = false;

    @Option @ToggleSetting(name = "Guild Welcome Message")
    public static boolean SEND_GUILD_WELCOME_MESSAGE = true;

    @Option @ToggleSetting(name = "Friends First on Tab")
    public static boolean FRIENDS_FIRST_IN_TAB = true;

    @Option @ToggleSetting(name = "Ping on DMs")
    public static boolean PING_ON_DM = true;

    @Option @ToggleSetting(name = "Party/Friend Request Popup")
    public static boolean SHOW_INGAME_CONFIRMATION_POPUP = true;
}
