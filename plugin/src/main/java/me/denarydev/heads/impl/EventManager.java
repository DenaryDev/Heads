/*
 * Copyright (c) 2023 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package me.denarydev.heads.impl;

import me.denarydev.heads.HeadsPlugin;
import me.denarydev.heads.api.events.DatabaseLoadEvent;
import me.denarydev.heads.api.events.PlayerClickHeadEvent;
import me.denarydev.heads.head.Head;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

@SuppressWarnings("deprecation")
public class EventManager {

    public static void callDatabaseLoadEvent(long loadTimeMs) {
        Bukkit.getGlobalRegionScheduler().execute(HeadsPlugin.getInstance(), () -> {
            HeadsPlugin.getInstance().logDebug("Calling DatabaseLoadEvent event");
            final var event = new DatabaseLoadEvent(loadTimeMs);
            Bukkit.getPluginManager().callEvent(event);

            HeadsPlugin.getInstance().logDebug("Calling legacy DatabaseLoadEvent event");
            final var legacyEvent = new me.arcaniax.hdb.api.DatabaseLoadEvent((int) loadTimeMs / 1000);
            Bukkit.getPluginManager().callEvent(legacyEvent);
        });
    }

    public static boolean callPlayerClickHeadEvent(Player player, Head head, ClickType click) {
        HeadsPlugin.getInstance().logDebug("Calling PlayerClickHeadEvent event");
        final var event = new PlayerClickHeadEvent(player, head, click);
        Bukkit.getPluginManager().callEvent(event);

        HeadsPlugin.getInstance().logDebug("Calling legacy PlayerClickHeadEvent event");
        final var legacyEvent = new me.arcaniax.hdb.api.PlayerClickHeadEvent(player, head);
        Bukkit.getPluginManager().callEvent(legacyEvent);

        return event.isCancelled() || legacyEvent.isCancelled();
    }
}
