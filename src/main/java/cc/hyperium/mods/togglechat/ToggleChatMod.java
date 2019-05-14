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

package cc.hyperium.mods.togglechat;

import cc.hyperium.Hyperium;
import cc.hyperium.event.EventBus;
import cc.hyperium.mods.AbstractMod;
import cc.hyperium.mods.togglechat.commands.CommandToggleChat;
import cc.hyperium.mods.togglechat.config.ToggleChatConfig;
import cc.hyperium.mods.togglechat.toggles.ToggleBaseHandler;

public final class ToggleChatMod extends AbstractMod {
    private final Metadata meta;

    private ToggleChatConfig configLoader;

    private ToggleBaseHandler toggleHandler;

    public ToggleChatMod() {
        Metadata metadata = new Metadata(this, "ToggleChat", "1", "boomboompower");
        this.meta = metadata;
    }

    public AbstractMod init() {
        this.configLoader = new ToggleChatConfig(this, Hyperium.folder);
        this.toggleHandler = new ToggleBaseHandler();
        this.toggleHandler.remake();
        EventBus.INSTANCE.register(new ToggleChatEvents(this));
        Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().registerCommand(new CommandToggleChat(this));
        this.configLoader.loadToggles();
        return this;
    }

    @Override
    public Metadata getModMetadata() {
        return this.meta;
    }

    public ToggleChatConfig getConfigLoader() {
        return this.configLoader;
    }

    public ToggleBaseHandler getToggleHandler() {
        return this.toggleHandler;
    }
}
