package cc.hyperium.mods.nickhider;

import cc.hyperium.config.ConfigOpt;
import org.apache.commons.lang3.RandomStringUtils;

public class NickHiderConfig {
    public static final NickHiderConfig INSTANCE = new NickHiderConfig();

    @ConfigOpt
    public String pseudo_key = RandomStringUtils.random(6, true, true);
    @ConfigOpt
    public boolean enabled = false;
    @ConfigOpt
    public boolean selfOnly = true;
    @ConfigOpt
    public boolean hideSkins = false;

    public String getPseudo_key() {
        return pseudo_key;
    }

    public void setPseudo_key(String pseudo_key) {
        this.pseudo_key = pseudo_key;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isSelfOnly() {
        return selfOnly;
    }

    public void setSelfOnly(boolean selfOnly) {
        this.selfOnly = selfOnly;
    }

    public boolean isHideSkins() {
        return hideSkins;
    }

    public void setHideSkins(boolean hideSkins) {
        this.hideSkins = hideSkins;
    }
}
