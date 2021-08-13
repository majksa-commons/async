/*
 *  async - cz.majksa.commons.async.AwaitedObject
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

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

/**
 * <p><b>Class {@link cz.majksa.commons.async.AwaitedObject}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
public class AwaitedObject<T> {

    private T object = null;

    /**
     * Set the object so the {@link #get()} will complete
     *
     * @param object the value that will be provided in {@link #get()}
     */
    public synchronized void set(T object) {
        this.object = object;
        notify();
    }

    /**
     * Get the object as completable future
     *
     * @return {@link java.util.concurrent.CompletableFuture}
     */
    @Nonnull
    public CompletableFuture<T> get() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return getNow();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Get the object now - as the object itself.
     * To get it as {@link java.util.concurrent.CompletableFuture}, use {@link #get()}
     *
     * @return {@link T}
     * @throws InterruptedException if any thread interrupted the current thread before or while the current thread was waiting for a notification. The interrupted status of the current thread is cleared when this exception is thrown.
     */
    public synchronized T getNow() throws InterruptedException {
        wait();
        return object;
    }

}
