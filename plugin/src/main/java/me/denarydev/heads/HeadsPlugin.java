/*
 * Copyright (c) 2022 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package me.denarydev.heads;

import lombok.Getter;
import me.denarydev.crystal.locale.LoaderType;
import me.denarydev.crystal.locale.LocaleManager;
import me.denarydev.crystal.locale.MessageSender;
import me.denarydev.heads.command.HeadsCommand;
import me.denarydev.heads.gui.GuiManager;
import me.denarydev.heads.head.HeadDBManager;
import me.denarydev.heads.impl.EventManager;
import me.denarydev.heads.impl.HeadsAPIImpl;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.spongepowered.configurate.ConfigurateException;

import java.util.Locale;
import java.util.UUID;

public class HeadsPlugin extends JavaPlugin {

    public static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    @Getter
    private static HeadsPlugin instance;
    @Getter
    private volatile LocaleManager locale;
    @Getter
    private GuiManager guiManager;
    @Getter
    private HeadDBManager headDBManager;

    @Getter
    private boolean loaded = false;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        if (!getDataFolder().exists() && !getDataFolder().mkdirs())
            return;

        this.locale = new LocaleManager(this.getSLF4JLogger(), this.getDataFolder(), LoaderType.YAML, new MessageSender() {
            @Override
            public void sendMessage(UUID uuid, String message) {
                final var player = Bukkit.getPlayer(uuid);
                if (player != null) player.sendMessage(message);
            }

            @Override
            public void sendMessage(UUID uuid, String... messages) {
                final var player = Bukkit.getPlayer(uuid);
                if (player != null) player.sendMessage(messages);
            }
        });
        this.guiManager = new GuiManager(this);
        this.headDBManager = new HeadDBManager(this);

        final var command = this.getCommand("heads");
        if (command != null) command.setExecutor(new HeadsCommand(this));

        onReload();
        new HeadsAPIImpl(this);
    }

    public void onReload() {
        loaded = false;
        guiManager.closeAll();

        try {
            Settings.load(getDataFolder().toPath());
            locale.load(new Locale(Settings.main().language));
            runAsync(headDBManager::loadDatabase);
            loaded = true;
        } catch (ConfigurateException ex) {
            getSLF4JLogger().error("Failed to load configurations", ex);
        }
    }

    @Override
    public void onDisable() {
        loaded = false;
    }

    public void logDebug(String s) {
        if (Settings.main().debug) log(s);
    }

    public void log(String s) {
        getSLF4JLogger().info(s);
    }

    public void logWarning(String s) {
        getSLF4JLogger().warn(s);
    }

    public void logError(String s) {
        getSLF4JLogger().error(s);
    }

    public void runSync(Runnable task) {
        getServer().getScheduler().runTask(this, task);
    }

    public void runAsync(Runnable task) {
        getServer().getScheduler().runTaskAsynchronously(this, task);
    }
}
