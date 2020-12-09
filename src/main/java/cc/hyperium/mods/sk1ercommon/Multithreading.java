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

package cc.hyperium.mods.sk1ercommon;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A utility class that implements multi-threading in a simple way.
 */
public class Multithreading {
    /**
     * A fixed thread pool consisting of 75 threads, allowing {@link Runnable}s to be run in parallel.
     *
     * @see Multithreading#runAsync(Runnable)
     */
    public static final ExecutorService POOL = Executors.newFixedThreadPool(75, new ThreadFactory() {
        final AtomicInteger counter = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, String.format("Thread %s", counter.incrementAndGet()));
        }
    });

    /**
     * A thread pool consisting of 10 threads that only run scheduled {@link Runnable}s.
     */
    public static final ScheduledExecutorService RUNNABLE_POOL = Executors.newScheduledThreadPool(10, new ThreadFactory() {
        private final AtomicInteger counter = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "Thread " + counter.incrementAndGet());
        }
    });

    /**
     * Schedules a repeating task that should be run with the specified initial delay and delay.
     *
     * @param r The task to run.
     * @param initialDelay How long it will wait before running the task for the first time.
     * @param delay How long it will wait to repeat the task after each execution.
     * @param unit The time unit that the initial delay and delay are in.
     */
    public static void schedule(Runnable r, long initialDelay, long delay, TimeUnit unit) {
        RUNNABLE_POOL.scheduleAtFixedRate(r, initialDelay, delay, unit);
    }

    /**
     * Schedules a task that should be executed after the specified amount of time.
     *
     * @param r The task to run.
     * @param delay How long to wait before executing the task.
     * @param unit The time unit that the delay parameter is in.
     */
    public static void schedule(Runnable r, long delay, TimeUnit unit) {
        RUNNABLE_POOL.schedule(r, delay, unit);
    }

    /**
     * Immediately runs the {@link Runnable} in a different thread.
     *
     * @param runnable The {@link Runnable} to run.
     */
    public static void runAsync(Runnable runnable) {
        POOL.execute(runnable);
    }
}
