package cc.hyperium.mods.autofriend;

import cc.hyperium.config.ToggleSetting;
import cc.hyperium.config.provider.IOptionSetProvider;
import rocks.rdil.simpleconfig.Option;

public class AutoFriendOptionsProvider implements IOptionSetProvider {
    public static final AutoFriendOptionsProvider INSTANCE = new AutoFriendOptionsProvider();

    @Override
    public String getName() {
        return "AutoFriend";
    }

    @Option @ToggleSetting(name = "Enable AutoFriend")
    public static boolean AUTOFRIEND_TOGGLE = false;

    @Option @ToggleSetting(name = "Show Friend Messages")
    public static boolean AUTOFRIEND_MESSAGES = true;
}
