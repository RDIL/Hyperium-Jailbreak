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

import com.google.common.reflect.TypeToken;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A class that handles the managing and dispatching of events.
 */
@SuppressWarnings("UnstableApiUsage")
public class EventBus {
    /**
     * The publicly usable instance of the EventBus.
     */
    public static final EventBus INSTANCE = new EventBus();

    private HashMap<Class<?>, CopyOnWriteArrayList<EventSubscriber>> subscriptions = new HashMap<>();

    /**
     * Register an object for its event listeners (methods annotated with {@link InvokeEvent}) to be listened for.
     *
     * @param obj The class instance that should be registered.
     */
    public void register(Object obj) {
        // also contains the class itself
        TypeToken<?> token = TypeToken.of(obj.getClass());
        Set superClasses = token.getTypes().rawTypes();

        // we also want to loop over the super classes, since declaredMethods only gets method in the class itself
        for (Object temp : superClasses) {
            Class<?> clazz = (Class<?>) temp;

            for (Method method : clazz.getDeclaredMethods()) {
                // all the information and error checking before the method is added such
                // as if it even is an event before the element even touches the HashMap
                if (method.getAnnotation(InvokeEvent.class) == null) {
                    continue;
                }
                if (method.getParameters()[0] == null) {
                    throw new IllegalArgumentException(method.getName() + " doesn't accept an event!");
                }

                Class<?> event = method.getParameters()[0].getType();
                Priority priority = method.getAnnotation(InvokeEvent.class).priority();
                method.setAccessible(true);

                // where the method gets added to the event key inside of the subscription hashmap
                // the arraylist is either sorted or created before the element is added
                if (this.subscriptions.containsKey(event)) {
                    // sorts array on insertion
                    this.subscriptions.get(event).add(new EventSubscriber(obj, method, priority));
                    this.subscriptions.get(event).sort(Comparator.comparingInt(a -> a.getPriority().value));
                } else {
                    // event hasn't been added before so it creates a new instance
                    // sorting does not matter here since there is no other elements to compete against
                    this.subscriptions.put(event, new CopyOnWriteArrayList<>());
                    this.subscriptions.get(event).add(new EventSubscriber(obj, method, priority));
                    this.subscriptions.get(event).sort(Comparator.comparingInt(a -> a.getPriority().value));
                }
            }
        }
    }

    /**
     * Unregisters a specific object.
     *
     * @param obj The object to unregister.
     */
    public void unregister(Object obj) {
        for (CopyOnWriteArrayList<EventSubscriber> map : subscriptions.values()) {
            map.removeIf(it -> it.getInstance() == obj);
        }
    }

    /**
     * Calls {@link EventBus#unregister(Object)} on all objects that are an instance of the specified class.
     *
     * @param clazz The class to unregister any of its instances.
     * @see EventBus#unregister(Object)
     */
    public void unregister(Class<?> clazz) {
        for (CopyOnWriteArrayList<EventSubscriber> map : subscriptions.values()) {
            map.removeIf(it -> it.getInstance().getClass() == clazz);
        }
    }

    /**
     * Trigger all event listeners for the passed event object.
     *
     * @param event The event to dispatch.
     */
    public void post(Object event) {
        if (event == null) {
            return;
        }

        for (EventSubscriber sub : this.subscriptions.getOrDefault(event.getClass(), new CopyOnWriteArrayList<>())) {
            try {
                sub.getMethod().invoke(sub.getInstance(), event);
            } catch (Exception InvocationTargetException) {
                InvocationTargetException.printStackTrace();
            }
        }
    }
}
