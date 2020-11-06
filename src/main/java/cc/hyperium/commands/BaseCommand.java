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

package cc.hyperium.commands;

import java.util.ArrayList;
import java.util.List;

/**
 * A command.
 */
public interface BaseCommand {
    /**
     * Get the name of the command.
     *
     * @return The name of the command.
     */
    String getName();

    /**
     * Get the usage of the command.
     *
     * @return The command's usage.
     */
    String getUsage();

    /**
     * Get a {@link java.util.List} of aliases for this command.
     * Each alias will be usable just like the normal command.
     *
     * @return The command's aliases.
     */
    default List<String> getCommandAliases() {
        return new ArrayList<>();
    }

    /**
     * Called on the execution of the command in chat.
     *
     * @param args The arguments passed to the command.
     * @throws CommandUsageException If the command's syntax is wrong. Optionally throw in your code.
     */
    void onExecute(String[] args) throws CommandUsageException;

    /**
     * Get a {@link java.util.List} of {@link java.lang.String}s that should be displayed on tab completion of the command.
     *
     * @param args The arguments currently typed in the chat.
     * @return A {@link java.util.List} of suggestions.
     */
    default List<String> onTabComplete(String[] args) {
        return null;
    }

    /**
     * If this command only serves to provide tab completion to a server-side command, return true.
     *
     * @return If the command is just for tab completion of server-side commands.
     */
    default boolean tabOnly() {
        return false;
    }
}
