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

package cc.hyperium.mods;

import cc.hyperium.addons.bossbar.BossbarAddon;
import cc.hyperium.addons.customcrosshair.CustomCrosshairAddon;
import cc.hyperium.addons.sidebar.SidebarAddon;
import cc.hyperium.mods.autofriend.AutofriendMod;
import cc.hyperium.mods.autogg.AutoGG;
import cc.hyperium.mods.autotext.AutoText;
import cc.hyperium.mods.chromahud.ChromaHUD;
import cc.hyperium.mods.blockoverlay.BlockOverlay;
import cc.hyperium.mods.itemphysic.ItemPhysicMod;
import cc.hyperium.mods.keystrokes.KeystrokesMod;
import cc.hyperium.mods.levelhead.Levelhead;
import cc.hyperium.mods.nickhider.NickHider;
import cc.hyperium.mods.oldanimations.OldAnimations;
import cc.hyperium.mods.tabtoggle.TabToggleMod;
import cc.hyperium.mods.timechanger.TimeChanger;
import cc.hyperium.mods.togglechat.ToggleChatMod;
import cc.hyperium.mods.glintcolorizer.GlintColorizer;
import me.semx11.autotip.Autotip;

public class HyperiumModIntegration {
    private final KeystrokesMod keystrokesMod;
    private final TimeChanger timeChanger;
    private final ToggleChatMod toggleChat;
    private final Levelhead levelhead;
    private final ChromaHUD chromaHUD;
    private final Autotip autotip;
    private final AutoGG autogg;
    private final GlintColorizer gc;
    private final AutoText autoText;
    private final CustomCrosshairAddon customCrosshairAddon;
    private final SidebarAddon sidebarAddon;
    private final BossbarAddon bossbarAddon;

    public HyperiumModIntegration() {
        this.chromaHUD = ((ChromaHUD) new ChromaHUD().init());
        this.levelhead = ((Levelhead) new Levelhead().init());
        this.toggleChat = ((ToggleChatMod) new ToggleChatMod().init());
        this.autoText = (AutoText) new AutoText().init();
        this.autotip = new Autotip();
        autotip.init();
        this.autogg = ((AutoGG) new AutoGG().init());
        new OldAnimations().init();
        new NickHider().init();
        new TabToggleMod().init();
        AutofriendMod autofriend = new AutofriendMod();
        FortniteCompassMod fncompass = new FortniteCompassMod();
        new ItemPhysicMod().init();
        this.gc = new GlintColorizer();
        BlockOverlay blockOverlay = new BlockOverlay();
        this.keystrokesMod = new KeystrokesMod();
        this.timeChanger = new TimeChanger();
        this.gc.init();
        this.keystrokesMod.init();
        this.timeChanger.init();
        autofriend.init();
        fncompass.init();
        blockOverlay.init();
        this.customCrosshairAddon = new CustomCrosshairAddon();
        this.sidebarAddon = new SidebarAddon();
        this.bossbarAddon = new BossbarAddon();
        this.customCrosshairAddon.init();
        this.sidebarAddon.init();
        this.bossbarAddon.init();
    }

    public KeystrokesMod getKeystrokesMod() {
        return keystrokesMod;
    }

    public TimeChanger getTimeChanger() {
        return timeChanger;
    }

    public ToggleChatMod getToggleChat() {
        return toggleChat;
    }

    public Levelhead getLevelhead() {
        return levelhead;
    }

    public ChromaHUD getChromaHUD() {
        return chromaHUD;
    }

    public Autotip getAutotip() {
        return autotip;
    }

    public AutoGG getAutoGG() {
        return autogg;
    }

    public GlintColorizer getGlintcolorizer() {
        return gc;
    }

    public AutoText getAutoText() {
        return autoText;
    }

    public CustomCrosshairAddon getCustomCrosshairAddon() {
        return customCrosshairAddon;
    }

    public SidebarAddon getSidebarAddon() {
        return sidebarAddon;
    }

    public BossbarAddon getBossbarAddon() {
        return bossbarAddon;
    }
}
