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

package cc.hyperium.mods.levelhead.commands;

import cc.hyperium.commands.BaseCommand;
import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;
import cc.hyperium.mods.levelhead.Levelhead;
import cc.hyperium.mods.levelhead.guis.LevelHeadGui;
import cc.hyperium.utils.ChatColor;

public class LevelHeadCommand implements BaseCommand {
    private final Levelhead mod;

    public LevelHeadCommand(Levelhead mod) {
        this.mod = mod;
    }

    @Override
    public String getName() {
        return "levelhead";
    }

    @Override
    public String getUsage() {
        return "/" + getName();
    }

    @Override
    public void onExecute(String[] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("dumpcache")) {
                this.mod.levelCache.clear();
                return;
            } else if (args[0].equalsIgnoreCase("toggle")) {
                this.mod.getConfig().setEnabled(!this.mod.getConfig().isEnabled());
                GeneralChatHandler.instance().sendMessage("Levelhead is now " + (this.mod.getConfig().isEnabled() ? ChatColor.GREEN + "enabled" : ChatColor.RED + "disabled"));
                return;
            }
        }
        new LevelHeadGui(this.mod).display();
    }
}
