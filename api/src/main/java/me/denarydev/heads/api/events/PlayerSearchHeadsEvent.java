/*
 * Copyright (c) 2025 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package me.denarydev.heads.api.events;

import me.denarydev.heads.api.head.Category;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerSearchHeadsEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancel = false;

    private final String params;
    private Category results;

    public PlayerSearchHeadsEvent(@NotNull final Player player, @NotNull String params, @NotNull final Category results) {
        super(player);
        this.params = params;
        this.results = results;
    }

    @NotNull
    public String getParams() {
        return params;
    }

    @NotNull
    public Category getResults() {
        return results;
    }

    public void setResults(Category results) {
        this.results = results;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
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
