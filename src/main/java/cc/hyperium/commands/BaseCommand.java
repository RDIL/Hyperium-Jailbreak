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
 * The interface all Hyperium commands must implement.
 */
public interface BaseCommand {
    /**
     * Get the name of the command without a slash in front of it.
     * 
     * @return The command's name without a slash.
     */
    String getName();

    /**
     * Get the command's usage, without a slash in front of it.
     * 
     * @return The command's usage.
     */
    String getUsage();

    /**
     * Get a {@link java.util.List} of command names that should function identically to this one.
     *  
     * @return The alias names.
     */
    default List<String> getCommandAliases() {
        return new ArrayList<>();
    }

    /**
     * Called when the command is run by the user. Execute any actions here.
     * 
     * @param args The arguments sent after the command.
     * @throws CommandUsageException If the user uses the command in a way that isn't supported.
     */
    void onExecute(String[] args) throws CommandUsageException;

    /**
     * Get a {@link java.util.List} of suggestions for tab completion based on the args already typed.
     * 
     * @param args The arguments already typed.
     * @return {@code null} if the command has no suggestions, or a {@link java.util.List} of suggestions.
     */
    default List<String> onTabComplete(String[] args) {
        return null;
    }

    /**
     * Get if this command's only purpose is to enable tab completion of a server-side command, or not.
     * If this is true, {@link BaseCommand#onExecute(String[])} will never be called.
     * 
     * @return If this command is only for tab completion.
     */
    default boolean tabOnly() {
        return false;
    }
}
