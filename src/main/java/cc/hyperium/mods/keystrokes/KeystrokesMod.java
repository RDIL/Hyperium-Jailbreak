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

package cc.hyperium.mods.keystrokes;

import cc.hyperium.Hyperium;
import cc.hyperium.event.EventBus;
import cc.hyperium.mods.AbstractMod;
import cc.hyperium.mods.keystrokes.config.KeystrokesSettings;
import cc.hyperium.mods.keystrokes.render.KeystrokesRenderer;

public class KeystrokesMod extends AbstractMod {
    private KeystrokesSettings config;
    private KeystrokesRenderer renderer;

    public KeystrokesMod() {}

    @Override
    public AbstractMod init() {
        this.config = new KeystrokesSettings(this, Hyperium.folder);
        this.config.load();
        this.renderer = new KeystrokesRenderer(this);
        EventBus.INSTANCE.register(this.renderer);
        Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().registerCommand(new CommandKeystrokes(this));

        return this;
    }

    public KeystrokesSettings getSettings() {
        return this.config;
    }

    public KeystrokesRenderer getRenderer() {
        return this.renderer;
    }
}
