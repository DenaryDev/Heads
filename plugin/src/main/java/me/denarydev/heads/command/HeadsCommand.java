/*
 * Copyright (c) 2023 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package me.denarydev.heads.command;

import lombok.RequiredArgsConstructor;
import me.denarydev.crystal.locale.Pair;
import me.denarydev.heads.HeadsPlugin;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
public class HeadsCommand implements CommandExecutor, TabCompleter {

    private final HeadsPlugin plugin;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        final var player = sender instanceof Player ? (Player) sender : null;

        if (!plugin.isLoaded()) {
            sender.sendRichMessage("error.plugin-not-loaded");
            return true;
        }

        if (args.length < 1) {
            if (!plugin.getHeadDBManager().isDatabaseLoaded()) {
                sender.sendRichMessage("error.database-not-loaded");
                return true;
            }
            if (sender.hasPermission("skyheads.command.open")) {
                if (player != null) {
                    plugin.getGuiManager().openOverview(player);
                } else {
                    sender.sendRichMessage("error.players-only");
                }
            } else {
                sender.sendRichMessage("error.no-permission");
            }
            return true;
        }

        switch (args[0]) {
            case "reload" -> {
                if (args.length == 1) {
                    if (sender.hasPermission("skyheads.command.reload")) {
                        plugin.onReload();
                        sender.sendRichMessage("command.reload.success");
                    } else {
                        sender.sendRichMessage("error.no-permission");
                    }
                } else {
                    usage(sender, label, "reload");
                }
            }
            case "search" -> {
                if (args.length >= 2) {
                    if (sender.hasPermission("skyheads.command.search")) {
                        if (player != null) {
                            final var searchArgs = new String[args.length - 1];
                            System.arraycopy(args, 1, searchArgs, 0, args.length - 1);
                            plugin.getHeadDBManager().doSearch(player, null, String.join(" ", searchArgs), false);
                        } else {
                            plugin.getLocale().getMessage("error.players-only");
                        }
                    } else {
                        sender.sendRichMessage("error.no-permission");
                    }
                } else {
                    usage(sender, label, "search");
                }
            }
            case "help" -> help(sender, label);
            default -> usage(sender, label, "help");
        }

        return true;
    }

    private void usage(CommandSender sender, String label, String s) {
        sender.sendMessage(HeadsPlugin.MINI_MESSAGE.deserialize(
                plugin.getLocale().getMessage("command.generic.usage").asString(),
                Placeholder.unparsed("label", label),
                Placeholder.parsed("usage", plugin.getLocale().getMessage("command", s, "usage").asString()))
        );
    }

    private void help(CommandSender sender, String label) {
        final var player = sender instanceof Player ? (Player) sender : null;
        final var helpLines = new ArrayList<String>();
        helpLines.add(plugin.getLocale().getMessage("command.help.header").asString());

        if (player != null && sender.hasPermission("skyheads.command.open")) {
            helpLines.add(plugin.getLocale().getMessage("command.help.command")
                    .placeholders(
                            Pair.of("usage", plugin.getLocale().getMessage("command.open.usage")
                                    .placeholders(Pair.of("label", label))
                                    .asString()),
                            Pair.of("info", plugin.getLocale().getMessage("command.open.info").asString())
                    )
                    .asString());
        }

        if (sender.hasPermission("skyheads.command.reload")) {
            helpLines.add(plugin.getLocale().getMessage("command.help.command")
                    .placeholders(
                            Pair.of("usage", plugin.getLocale().getMessage("command.reload.usage")
                                    .placeholders(Pair.of("label", label))
                                    .asString()),
                            Pair.of("info", plugin.getLocale().getMessage("command.reload.info").asString())
                    )
                    .asString());
        }

        helpLines.add(" ");

        helpLines.forEach(sender::sendRichMessage);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        final var aliases = new ArrayList<String>();

        if (args.length == 1) {
            if (args[0].isEmpty()) {
                if (sender.hasPermission("skyheads.command.reload")) aliases.add("reload");
                if (sender.hasPermission("skyheads.command.search")) aliases.add("search");
            } else {
                String arg = args[0].toLowerCase(Locale.ROOT);
                if ("reload".contains(arg) && sender.hasPermission("skyheads.command.reload")) aliases.add("reload");
                if ("search".contains(arg) && sender.hasPermission("skyheads.command.search")) aliases.add("search");
            }
        }

        return aliases;
    }
}
