package me.denarydev.heads.api.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class DatabaseLoadEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final int time;

    public DatabaseLoadEvent(int amount) {
        this.time = amount;
    }

    public int getLoadTime() {
        return this.time;
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
