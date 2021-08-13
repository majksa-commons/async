/*
 *  async - cz.majksa.commons.async.ScheduledTaskTest
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

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class ScheduledTaskTest {

    @Test
    void simple() {
        final Calendar instance = Calendar.getInstance();
        instance.add(Calendar.SECOND, 3);
        final AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        final ScheduledTask schedule = ScheduledTask.schedule(instance.getTime(), () -> atomicBoolean.set(true));
        schedule.get().join();
        assertTrue(atomicBoolean.get());
    }

    @Test
    void reschedule() {
        final Calendar instance = Calendar.getInstance();
        instance.add(Calendar.SECOND, 3);
        final AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        final ScheduledTask schedule = ScheduledTask.schedule(instance.getTime(), () -> atomicBoolean.set(true));
        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 3);
        schedule.setDate(calendar.getTime());
        schedule.get().join();
        assertTrue(atomicBoolean.get());
    }

    @Test
    @SneakyThrows
    void serialize() {
        // Initialize tasks
        ScheduledTask.setExecutor(5);
        final ArrayList<ScheduledTask> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            final Calendar instance = Calendar.getInstance();
            instance.add(Calendar.SECOND, 3);
            // Create the tasks that just increases the atomic integer by one
            list.add(ScheduledTask.schedule(instance.getTime(), new MyTask()));
        }
        writeAll(list);
        list.forEach(ScheduledTask::cancel);
        final List<ScheduledTask> scheduledTasks = loadAll();
        clearAll();
        // Make sure they all finished
        final List<CompletableFuture<Void>> collect = scheduledTasks
                .stream()
                .map(ScheduledTask::get)
                .collect(Collectors.toList());
        for (CompletableFuture<Void> future : collect) {
            future.get();
        }
        assertEquals(5, MyTask.getCounter());
    }

    public static final File FOLDER = new File("serializable");

    @SneakyThrows
    private void writeAll(List<ScheduledTask> tasks) {
        if (!FOLDER.exists()) {
            assertTrue(FOLDER.mkdirs());
        }
        for (int i = 0; i < tasks.size(); i++) {
            try (FileOutputStream stream = new FileOutputStream(new File(FOLDER, i + ".ser"))) {
                try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(stream)) {
                    objectOutputStream.writeObject(tasks.get(i));
                }
            }
        }
    }

    @SneakyThrows
    private List<ScheduledTask> loadAll() {
        final List<ScheduledTask> list = new ArrayList<>();
        for (File file : Objects.requireNonNull(FOLDER.listFiles())) {
            try (FileInputStream stream = new FileInputStream(file)) {
                try (ObjectInputStream objectInputStream = new ObjectInputStream(stream)) {
                    final ScheduledTask scheduledTask = (ScheduledTask) objectInputStream.readObject();
                    list.add(scheduledTask);
                }
            }
        }
        return list;
    }

    @SneakyThrows
    private void clearAll() {
        for (File file : Objects.requireNonNull(FOLDER.listFiles())) {
            if (!file.delete()) {
                file.deleteOnExit();
            }
        }
        if (!FOLDER.delete()) {
            FOLDER.deleteOnExit();
        }
    }

}