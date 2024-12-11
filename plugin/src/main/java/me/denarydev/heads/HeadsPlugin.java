/*
 * Copyright (c) 2024 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package me.denarydev.heads;

import lombok.Getter;
import me.denarydev.heads.command.HeadsCommand;
import me.denarydev.heads.head.HeadDBManager;
import me.denarydev.heads.api.HeadsAPIImpl;
import me.denarydev.heads.menu.MenuManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.spongepowered.configurate.ConfigurateException;

public class HeadsPlugin extends JavaPlugin {

    @Getter
    private static HeadsPlugin instance;
    @Getter
    private MenuManager menuManager;
    @Getter
    private HeadDBManager headDBManager;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        if (!getDataFolder().exists() && !getDataFolder().mkdirs())
            return;

        this.menuManager = new MenuManager(this);
        this.headDBManager = new HeadDBManager(this);

        final var command = new HeadsCommand(this);
        Bukkit.getCommandMap().register(this.getName().toLowerCase(), command);

        try {
            Settings.load(getDataFolder().toPath());
            runAsync(headDBManager::loadDatabase);
        } catch (ConfigurateException ex) {
            getSLF4JLogger().error("Failed to load configurations", ex);
        }

        new HeadsAPIImpl(this);
    }

    public void logDebug(String s) {
        if (Settings.main().debug) getLogger().info("DEBUG: " + s);
    }

    public void runAsync(Runnable task) {
        getServer().getAsyncScheduler().runNow(this, (st) -> task.run());
    }

    public void sync(Runnable task) {
        getServer().getGlobalRegionScheduler().execute(this, task);
    }
}
