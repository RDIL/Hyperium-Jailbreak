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

package cc.hyperium.handlers.handlers.keybinds;

import cc.hyperium.Hyperium;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

public class HyperiumBind {
    private final int defaultKeyCode;

    private final String description;
    private int key;

    private boolean wasPressed;

    private boolean conflicted = false;
    protected boolean conflictExempt = false;

    public HyperiumBind(String description, int key) {
        this(description, key, "Hyperium");
    }

    public HyperiumBind(String description, int key, String category) {
        this.defaultKeyCode = key;

        this.description = description;
        this.key = key;
    }

    public int getKeyCode() {
        return this.key;
    }

    public void setKeyCode(int key) {
        this.key = key;
    }

    public int getDefaultKeyCode() {
        return this.defaultKeyCode;
    }

    public String getKeyDescription() {
        return this.description;
    }

    protected String getRealDescription() {
        return this.description;
    }

    public void setWasPressed(boolean wasPressed) {
        this.wasPressed = wasPressed;
    }

    public void setConflicted(boolean conflicted) {
        this.conflicted = conflicted;
    }

    public boolean isConflicted() {
        return this.conflicted;
    }

    public boolean wasPressed() {
        return this.wasPressed;
    }

    public boolean capitalizeDescription() {
        return true;
    }

    public void onPress() {
        // We want these to be changed
    }

    public void onRelease() {
        // We want these to be changed
    }

    public void detectConflicts() {
        setConflicted(false);

        int currentKeyCode = this.getKeyCode();

        if (currentKeyCode == 0 || conflictExempt) {
            // Allow multiple binds to be set to NONE.
            return;
        }

        List<HyperiumBind> otherBinds = new ArrayList<>(Hyperium.INSTANCE.getHandlers().getKeybindHandler().getKeybinds().values());
        otherBinds.remove(this);

        // Check for conflicts with Minecraft binds.
        for (KeyBinding keyBinding : Minecraft.getMinecraft().gameSettings.keyBindings) {
            int keyCode = keyBinding.getKeyCode();

            if (currentKeyCode == keyCode) {
                // There is a conflict!
                setConflicted(true);
            }
        }

        // Check for conflicts with other Hyperium binds.
        for (HyperiumBind hyperiumBind : otherBinds) {
            if (hyperiumBind.conflictExempt) {
                continue;
            }
            int keyCode = hyperiumBind.getKeyCode();

            if (currentKeyCode == keyCode) {
                // There is a conflict!
                setConflicted(true);
            }
        }
    }
}
