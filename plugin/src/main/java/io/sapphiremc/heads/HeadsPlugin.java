/*
 * Copyright (c) 2022 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package io.sapphiremc.heads;

import com.songoda.core.gui.Gui;
import com.songoda.core.gui.GuiManager;
import io.sapphiremc.heads.command.HeadsCommand;
import io.sapphiremc.heads.config.ConfigManager;
import io.sapphiremc.heads.hdb.HeadDBManager;
import io.sapphiremc.heads.impl.HeadsAPIImpl;
import io.sapphiremc.heads.lang.LanguageManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class HeadsPlugin extends JavaPlugin {

    @Getter
    private static HeadsPlugin instance;
    @Getter
    private final GuiManager guiManager = new GuiManager(this);
    @Getter
    private ConfigManager configManager;
    @Getter
    private volatile LanguageManager languageManager;
    @Getter
    private HeadDBManager headDBManager;

    @Getter
    private FileConfiguration configuration;

    @Getter
    private boolean load = false;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        if (!getDataFolder().exists() && !getDataFolder().mkdirs())
            return;

        this.configManager = new ConfigManager(this);
        this.languageManager = new LanguageManager(this);
        this.headDBManager = new HeadDBManager(this);

        PluginCommand command = this.getCommand("heads");
        if (command != null) command.setExecutor(new HeadsCommand(this));

        onReload();
        new HeadsAPIImpl(this);
        logDebug("Enabling complete");
    }

    public void onReload() {
        load = false;
        guiManager.closeAll();

        configuration = configManager.getConfig("config.yml");
        languageManager.reload(configuration);
        runAsync(headDBManager::loadDatabase);

        load = true;
    }

    @Override
    public void onDisable() {
        load = false;
    }

    public void log(String s) {
        Bukkit.getConsoleSender().sendMessage("§7[§bHeads §8| §aInfo§7] §f" + s);
    }

    public void logDebug(String s) {
        if (configuration.getBoolean("debug", false)) Bukkit.getConsoleSender().sendMessage("§7[§bHeads §8| §fDebug§7] §o" + s);
    }

    public void logWarning(String s) {
        Bukkit.getConsoleSender().sendMessage("§7[§bHeads §8| §6Warn§7] §e" + s);
    }

    public void logError(String s) {
        Bukkit.getConsoleSender().sendMessage("§7[§bHeads §8| §cError§7] §6" + s);
    }

    public void showGUI(Player player, Gui gui) {
        getServer().getScheduler().runTask(this, () -> guiManager.showGUI(player, gui));
    }

    public void runAsync(Runnable runnable) {
        getServer().getScheduler().runTaskAsynchronously(this, runnable);
    }
}
