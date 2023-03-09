package me.denarydev.heads.impl;

import me.denarydev.heads.api.events.DatabaseLoadEvent;
import me.denarydev.heads.api.events.PlayerClickHeadEvent;
import me.denarydev.heads.head.Head;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@SuppressWarnings("deprecation")
public class EventManager {

    public static void callDatabaseLoadEvent(int loadTime) {
        final var event = new DatabaseLoadEvent(loadTime);
        Bukkit.getPluginManager().callEvent(event);

        final var legacyEvent = new me.arcaniax.hdb.api.DatabaseLoadEvent(loadTime);
        Bukkit.getPluginManager().callEvent(legacyEvent);
    }

    public static boolean callPlayerClickHeadEvent(Player player, Head head) {
        final var event = new PlayerClickHeadEvent(player, head);
        Bukkit.getPluginManager().callEvent(event);

        final var legacyEvent = new me.arcaniax.hdb.api.PlayerClickHeadEvent(player, head);
        Bukkit.getPluginManager().callEvent(legacyEvent);

        return event.isCancelled() || legacyEvent.isCancelled();
    }
}
