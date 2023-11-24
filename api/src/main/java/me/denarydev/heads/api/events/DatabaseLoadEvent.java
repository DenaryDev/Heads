/*
 * Copyright (c) 2023 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package me.denarydev.heads.api.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public class DatabaseLoadEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final long loadTimeMs;

    public DatabaseLoadEvent(long loadTimeMs) {
        this.loadTimeMs = loadTimeMs;
    }

    public long getLoadTimeMs() {
        return loadTimeMs;
    }

    public Duration getLoadTime() {
        return Duration.ofMillis(loadTimeMs);
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
