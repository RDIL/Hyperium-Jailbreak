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

/**
 * Thrown when a command is used in a way that the command doesn't support.
 */
public class CommandUsageException extends Exception {
    /**
     * Creates a new instance of the exception.
     */
    public CommandUsageException() {
        super();
    }

    /**
     * Creates a new instance of the exception.
     * 
     * @param reason The reason the exception was thrown.
     */
    public CommandUsageException(String reason) {
        super(reason);
    }
}
