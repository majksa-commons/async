/*
 *  async - cz.majksa.commons.async.ScheduledTask
 *  Copyright (C) 2021  Majksa
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package cz.majksa.commons.async;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <p><b>Class {@link ScheduledTask}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
@Getter
public class ScheduledTask implements Runnable, Serializable {

    private static final long serialVersionUID = -955331015966361424L;

    @Nonnull
    private static ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(1);
    public static void setExecutor(@Nonnull ScheduledExecutorService executor) {
        ScheduledTask.executor = executor;
    }

    public static void setExecutor(int corePoolSize) {
        setExecutor(new ScheduledThreadPoolExecutor(corePoolSize));
    }


    @Setter
    protected SerializableRunnable task;

    @Nonnull
    protected Date date;

    protected ScheduledFuture<?> future;

    protected AwaitedObject<Void> awaitedObject = new AwaitedObject<>();

    public ScheduledTask(@Nonnull Date date, @Nonnull SerializableRunnable task) {
        this.date = date;
        this.task = task;
        schedule();
    }

    public void setDate(@Nonnull Date date) {
        this.date = date;
        reschedule();
    }

    @Override
    public void run() {
        awaitedObject.set(null);
        task.run();
    }

    public void cancel() {
        future.cancel(true);
    }

    public void finish() {
        cancel();
        run();
    }

    @Nonnull
    public CompletableFuture<Void> get() {
        return awaitedObject.get();
    }

    protected void schedule() {
        future = executor.schedule(this, date.getTime() - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    protected void reschedule() {
        cancel();
        schedule();
    }

    private void writeObject(@Nonnull ObjectOutputStream stream) throws IOException {
        stream.writeObject(date);
        stream.writeObject(task);
    }

    private void readObject(@Nonnull ObjectInputStream stream) throws IOException, ClassNotFoundException {
        date = (Date) stream.readObject();
        task = (SerializableRunnable) stream.readObject();
        awaitedObject = new AwaitedObject<>();
        schedule();
    }

    /**
     * Schedule a new task that will be called on the {@link java.util.Date} provided
     *
     * @param date the date of execution
     * @param task the task that will be executed
     * @return the scheduled task object
     */
    @Nonnull
    public static ScheduledTask schedule(@Nonnull Date date, @Nonnull SerializableRunnable task) {
        return new ScheduledTask(date, task);
    }

}
