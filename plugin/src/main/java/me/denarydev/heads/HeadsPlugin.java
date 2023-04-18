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
import me.denarydev.heads.impl.HeadsAPIImpl;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.spongepowered.configurate.ConfigurateException;

import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
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

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        if (!getDataFolder().exists() && !getDataFolder().mkdirs())
            return;

        this.locale = new LocaleManager(this.getSLF4JLogger(), this.getDataFolder(), LoaderType.HOCON, new MessageSender() {
            @Override
            public void sendMessage(UUID uuid, String message) {
                final var player = Bukkit.getPlayer(uuid);
                Objects.requireNonNullElseGet(player, Bukkit::getConsoleSender).sendRichMessage(message);
            }

            @Override
            public void sendMessage(UUID uuid, String... messages) {
                final var player = Bukkit.getPlayer(uuid);
                if (player != null) Arrays.stream(messages).forEach(msg -> Objects.requireNonNullElseGet(player, Bukkit::getConsoleSender).sendRichMessage(msg));
            }
        });
        this.guiManager = new GuiManager(this);
        this.headDBManager = new HeadDBManager(this);

        final var command = this.getCommand("heads");
        if (command != null) command.setExecutor(new HeadsCommand(this));

        try {
            Settings.load(getDataFolder().toPath());
            locale.load(new Locale(Settings.main().language));
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
        getServer().getScheduler().runTaskAsynchronously(this, task);
    }

    public void sync(Runnable task) {
        getServer().getScheduler().runTask(this, task);
    }
}
