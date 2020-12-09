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
 * An event that can be cancelled.
 *
 * @see cc.hyperium.event.Event
 * @see CancellableEvent#cancel()
 */
public class CancellableEvent extends Event {
    private boolean cancelled;

    /**
     * Sets if the event is cancelled or not.
     *
     * @param cancelled If the event should be cancelled or not.
     */
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    /**
     * Get if the event is cancelled or not.
     *
     * @return If the event is cancelled or not.
     */
    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * Cancels the event.
     */
    public void cancel() {
        this.setCancelled(true);
    }
}
