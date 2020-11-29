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

package cc.hyperium.event;

/**
 * The priority an event should be executed with.
 * Higher priorities are run before lower ones.
 */
public enum Priority {
    /**
     * The highest possible priority. This will make the event run first or close to first.
     */
    HIGH(-1),

    /**
     * The default priority, run between {@link Priority#HIGH} and {@link Priority#LOW}.
     */
    NORMAL(0),

    /**
     * The lowest possible priority. This will make the event run last or close to last.
     */
    LOW(1);

    /**
     * The value of the priority, higher numbers have lower priority.
     */
    public final int value;

    /**
     * Basic constructor.
     *
     * @param value The value of the priority.
     * @see Priority#value
     */
    Priority(int value) {
        this.value = value;
    }

    /**
     * Get the value of the priority.
     *
     * @return The integer value.
     */
    public int getValue() {
        return this.value;
    }
}
