package me.semx11.autotip.core;

import static java.util.concurrent.TimeUnit.SECONDS;

import cc.hyperium.mods.sk1ercommon.Multithreading;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;

public class TaskManager {
    private final Map<TaskType, Future<?>> tasks;

    public TaskManager() {
        this.tasks = new ConcurrentHashMap<>();
    }

    <T> T scheduleAndAwait(Callable<T> callable, long delay) {
        try {
            return Multithreading.RUNNABLE_POOL.schedule(callable, delay, SECONDS).get();
        } catch (InterruptedException | ExecutionException e) {
            return null;
        }
    }

    public void executeTask(TaskType type, Runnable task) {
        if (tasks.containsKey(type)) {
            return;
        }
        Future<?> future = Multithreading.POOL.submit(task);
        tasks.put(type, future);
        this.catchFutureException(type, future);
    }

    void addRepeatingTask(TaskType type, Runnable command, long delay, long period) {
        if (tasks.containsKey(type)) {
            return;
        }
        ScheduledFuture<?> future = Multithreading.RUNNABLE_POOL.scheduleAtFixedRate(command, delay, period, SECONDS);
        tasks.put(type, future);
        this.catchFutureException(type, future);
    }

    void cancelTask(TaskType type) {
        if (tasks.containsKey(type)) {
            tasks.get(type).cancel(true);
            tasks.remove(type);
        }
    }

    private void catchFutureException(TaskType type, Future<?> future) {
        Multithreading.runAsync(() -> {
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

    public enum TaskType {
        LOGIN, KEEP_ALIVE, TIP_WAVE, TIP_CYCLE, LOGOUT
    }
}
