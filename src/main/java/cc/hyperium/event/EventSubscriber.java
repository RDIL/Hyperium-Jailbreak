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

import com.google.common.base.Preconditions;
import java.lang.reflect.Method;

public final class EventSubscriber {
    private final Object instance;
    private final Method method;
    private final Priority priority;

    EventSubscriber(Object instance, Method method, Priority priority) {
        Preconditions.checkNotNull(instance, "instance cannot be null");
        Preconditions.checkNotNull(method, "method cannot be null");
        Preconditions.checkNotNull(priority, "priority cannot be null");

        this.instance = instance;
        this.method = method;
        this.priority = priority;
    }

    public final Object getInstance() {
        return this.instance;
    }

    public final Method getMethod() {
        return this.method;
    }

    final Priority getPriority() {
        return this.priority;
    }

    public final EventSubscriber copy(Object instance, Method method, Priority priority) {
        Preconditions.checkNotNull(instance, "instance");
        Preconditions.checkNotNull(method, "method");
        Preconditions.checkNotNull(priority, "priority");

        return new EventSubscriber(instance, method, priority);
    }

    @Override
    public String toString() {
        return "EventSubscriber(instance=" + this.instance + ", method=" + this.method + ", priority=" + this.priority + ")";
    }

    @Override
    public int hashCode() {
        return (this.instance.hashCode() * 31 + this.method.hashCode()) * 31 + this.priority.hashCode();
    }

    @Override
    public boolean equals(Object subscriberIn) {
        if (this != subscriberIn) {
            if (subscriberIn instanceof EventSubscriber) {
                EventSubscriber eventSubscriber = (EventSubscriber) subscriberIn;

                return this.instance.equals(eventSubscriber.instance) &&
                    this.method.equals(eventSubscriber.method) &&
                    this.priority.equals(eventSubscriber.priority);
            }

            return false;
        } else {
            return true;
        }
    }
}
