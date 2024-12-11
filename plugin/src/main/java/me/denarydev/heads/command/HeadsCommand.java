/*
 * Copyright (c) 2024 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package me.denarydev.heads.command;

import me.denarydev.heads.HeadsPlugin;
import me.denarydev.heads.Settings;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurateException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HeadsCommand extends Command {

    private final HeadsPlugin plugin;

    public HeadsCommand(HeadsPlugin plugin) {
        super("heads");
        setAliases(List.of("hdb", "head"));
        this.plugin = plugin;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        final var player = sender instanceof Player ? (Player) sender : null;

        if (args.length < 1) {
            if (sender.hasPermission("heads.open")) {
                if (!plugin.getHeadDBManager().isDatabaseLoaded()) {
                    sender.sendRichMessage(Settings.messages().errors.databaseNotLoaded);
                    return true;
                }
                if (player != null) {
                    plugin.getMenuManager().openOverview(player);
                } else {
                    sender.sendRichMessage(Settings.messages().errors.playersOnly);
                }
            } else {
                sender.sendRichMessage(Settings.messages().errors.noPermission);
            }
            return true;
        }

        switch (args[0]) {
            case "reload" -> {
                if (args.length == 1) {
                    if (sender.hasPermission("heads.reload")) {
                        plugin.getMenuManager().closeAll();

                        try {
                            Settings.load(plugin.getDataFolder().toPath());
                            plugin.runAsync(() -> plugin.getHeadDBManager().loadDatabase((loadTime, loadedHeads) -> {
                                if (loadTime > 0L) {
                                    sender.sendRichMessage(Settings.messages().commands.reload.database.success,
                                            Placeholder.unparsed("time", String.valueOf(loadTime)),
                                            Placeholder.unparsed("amount", String.valueOf(loadedHeads))
                                    );
                                } else {
                                    sender.sendRichMessage(Settings.messages().commands.reload.database.failed);
                                }
                            }));
                        } catch (ConfigurateException ex) {
                            plugin.getSLF4JLogger().error("Failed to load configurations", ex);
                            sender.sendRichMessage(Settings.messages().errors.invalidConfiguration);
                        }
                        sender.sendRichMessage(Settings.messages().commands.reload.success);
                    } else {
                        sender.sendRichMessage(Settings.messages().errors.noPermission);
                    }
                } else {
                    sender.sendRichMessage(Settings.messages().commands.reload.usage, Placeholder.unparsed("label", label));
                }
            }
            case "search" -> {
                if (args.length >= 2) {
                    if (sender.hasPermission("heads.search")) {
                        if (!plugin.getHeadDBManager().isDatabaseLoaded()) {
                            sender.sendRichMessage(Settings.messages().errors.databaseNotLoaded);
                            return true;
                        }
                        if (player != null) {
                            final var searchArgs = new String[args.length - 1];
                            System.arraycopy(args, 1, searchArgs, 0, args.length - 1);
                            plugin.getHeadDBManager().doSearch(player, null, String.join(" ", searchArgs), false);
                        } else {
                            sender.sendRichMessage(Settings.messages().errors.playersOnly);
                        }
                    } else {
                        sender.sendRichMessage(Settings.messages().errors.noPermission);
                    }
                } else {
                    sender.sendRichMessage(Settings.messages().commands.search.usage, Placeholder.unparsed("label", label));
                }
            }
            case "help" -> help(sender, label);
            default -> {
                return false;
            }
        }

        return true;
    }

    private void help(CommandSender sender, String label) {
        final var player = sender instanceof Player ? (Player) sender : null;
        final var helpLines = new ArrayList<Component>();
        final var mm = MiniMessage.miniMessage();
        helpLines.add(mm.deserialize(Settings.messages().commands.help.header));

        if (player != null) {
            if (sender.hasPermission("heads.open")) {
                helpLines.add(mm.deserialize(Settings.messages().commands.help.entry,
                        Placeholder.component("usage", mm.deserialize(Settings.messages().commands.open.usage,
                                Placeholder.unparsed("label", label))),
                        Placeholder.parsed("info", Settings.messages().commands.open.info)
                ));
            }
            if (sender.hasPermission("heads.search")) {
                helpLines.add(mm.deserialize(Settings.messages().commands.help.entry,
                        Placeholder.component("usage", mm.deserialize(Settings.messages().commands.search.usage,
                                Placeholder.unparsed("label", label))),
                        Placeholder.parsed("info", Settings.messages().commands.search.info)
                ));
            }
        }

        if (sender.hasPermission("heads.reload")) {
            helpLines.add(mm.deserialize(Settings.messages().commands.help.entry,
                    Placeholder.component("usage", mm.deserialize(Settings.messages().commands.reload.usage,
                            Placeholder.unparsed("label", label))),
                    Placeholder.parsed("info", Settings.messages().commands.reload.info)
            ));
        }

        if (helpLines.size() == 1) {
            helpLines.clear();
            helpLines.add(mm.deserialize(Settings.messages().errors.noPermission));
        }

        helpLines.forEach(sender::sendMessage);
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        final var aliases = new ArrayList<String>();

        if (args.length == 1) {
            if (args[0].isEmpty()) {
                if (sender.hasPermission("heads.reload")) aliases.add("reload");
                if (sender.hasPermission("heads.search")) aliases.add("search");
            } else {
                String arg = args[0].toLowerCase(Locale.ROOT);
                if ("reload".contains(arg) && sender.hasPermission("heads.reload")) aliases.add("reload");
                if ("search".contains(arg) && sender.hasPermission("heads.search")) aliases.add("search");
            }
        } else if (args.length == 2) {
            if (args[1].isEmpty()) {
                if (args[0].equals("search") && sender.hasPermission("heads.search")) {
                    aliases.add("[args]");
                }
            }
        }

        return aliases;
    }
}
