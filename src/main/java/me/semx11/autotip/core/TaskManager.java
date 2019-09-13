package me.semx11.autotip.core;

import static java.util.concurrent.TimeUnit.SECONDS;

import cc.hyperium.mods.sk1ercommon.Multithreading;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;

public class TaskManager {
    private final ExecutorService executor = Multithreading.POOL;
    private final ScheduledExecutorService scheduler = Multithreading.RUNNABLE_POOL;
    private final Map<TaskType, Future> tasks;

    public TaskManager() {
        this.tasks = new ConcurrentHashMap<>();
    }

    <T> T scheduleAndAwait(Callable<T> callable, long delay) {
        try {
            return scheduler.schedule(callable, delay, SECONDS).get();
        } catch (InterruptedException | ExecutionException e) {
            return null;
        }
    }

    public void executeTask(TaskType type, Runnable task) {
        if (tasks.containsKey(type)) {
            return;
        }
        Future<?> future = executor.submit(task);
        tasks.put(type, future);
        this.catchFutureException(type, future);
    }

    void addRepeatingTask(TaskType type, Runnable command, long delay, long period) {
        if (tasks.containsKey(type)) {
            return;
        }
        ScheduledFuture future = scheduler.scheduleAtFixedRate(command, delay, period, SECONDS);
        tasks.put(type, future);
        this.catchFutureException(type, future);
    }

    void cancelTask(TaskType type) {
        if (tasks.containsKey(type)) {
            tasks.get(type).cancel(true);
            tasks.remove(type);
        }
    }

    private void catchFutureException(TaskType type, Future future) {
        this.executor.execute(() -> {
            try {
                future.get();
            } catch (CancellationException ignored) {
                // Manual cancellation
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            } finally {
                tasks.remove(type);
            }
        });
    }

    private ThreadFactory getFactory(String name) {
        return new ThreadFactoryBuilder().setNameFormat(name).setUncaughtExceptionHandler((t, e) -> e.printStackTrace()).build();
    }

    public enum TaskType {
        LOGIN, KEEP_ALIVE, TIP_WAVE, TIP_CYCLE, LOGOUT
    }
}
