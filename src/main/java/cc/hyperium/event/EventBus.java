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

@SuppressWarnings("UnstableApiUsage")
public class EventBus {
    public static final EventBus INSTANCE = new EventBus();
    private HashMap<Class<?>, CopyOnWriteArrayList<EventSubscriber>> subscriptions = new HashMap<>();
    public void register(Object obj) {
        // also contains the class itself
        TypeToken<?> token = TypeToken.of(obj.getClass());
        Set superClasses = token.getTypes().rawTypes();

        // we also want to loop over the super classes, since declaredMethods only gets method in the class itself
        for (Object temp : superClasses) {
            Class<?> clazz = (Class<?>) temp;

            // iterates though all the methods in the class
            for (Method method : clazz.getDeclaredMethods()) {
                // all the information and error checking before the method is added such
                // as if it even is an event before the element even touches the HashMap
                if (method.getAnnotation(InvokeEvent.class) == null) {
                    continue;
                }
                if (method.getParameters()[0] == null) {
                    throw new IllegalArgumentException("Couldn't find parameter inside of " + method.getName() + "!");
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

    public void unregister(Object obj) {
        this.subscriptions.values().forEach(map -> map.removeIf(it -> it.getInstance() == obj));
    }

    public void unregister(Class<?> clazz) {
        this.subscriptions.values().forEach(map -> map.removeIf(it -> it.getInstance().getClass() == clazz));
    }

    public void post(Object event) {
        if (event == null) {
            return;
        }
        this.subscriptions.getOrDefault(event.getClass(), new CopyOnWriteArrayList<>()).forEach((sub) -> {
            try {
                sub.getMethod().invoke(sub.getInstance(), event);
            } catch (Exception InvocationTargetException) {
                InvocationTargetException.printStackTrace();
            }
        });
    }
}
