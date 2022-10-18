/*
 * Copyright (c) 2022 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package io.sapphiremc.heads.command;

import io.sapphiremc.heads.HeadsPlugin;
import io.sapphiremc.heads.gui.GUIOverview;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
public class HeadsCommand implements CommandExecutor, TabCompleter {

    private final HeadsPlugin plugin;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player p = sender instanceof Player ? (Player) sender : null;

        if (args.length < 1) {
            if (!plugin.getHeadDBManager().isDatabaseLoaded()) {
                plugin.getLanguageManager().getMessage(p, "error.database-not-loaded").send(sender);
                return true;
            }
            if (sender.hasPermission("heads.command.open")) {
                if (sender instanceof Player player) {
                    plugin.getGuiManager().showGUI(player, new GUIOverview(player));
                } else {
                    plugin.getLanguageManager().getMessage("error.players-only").send(sender);
                }
            } else {
                plugin.getLanguageManager().getMessage(p, "error.no-permission").send(sender);
            }
            return true;
        }

        switch (args[0]) {
            case "reload" -> {
                if (args.length == 1) {
                    if (sender.hasPermission("heads.command.reload")) {
                        plugin.onReload();
                        plugin.getLanguageManager().getMessage(p, "command.reload.success").send(sender);
                    } else {
                        plugin.getLanguageManager().getMessage(p, "error.no-permission").send(sender);
                    }
                } else {
                    usage(sender, label, "reload");
                }
            }
            case "search" -> {
                if (args.length >= 2) {
                    if (sender.hasPermission("heads.command.search")) {
                        if (sender instanceof Player player) {
                            StringBuilder builder = new StringBuilder();
                            List<String> searchArgs = new ArrayList<>(Arrays.stream(args).toList());
                            searchArgs.remove(args[0]);
                            searchArgs.remove(args[1]);
                            for (String searchArg : searchArgs) {
                                if (builder.length() > 1) {
                                    builder.append(" ");
                                }
                                builder.append(searchArg);
                            }
                            plugin.getHeadDBManager().doSearch(player, null, builder.toString(), false);
                        } else {
                            plugin.getLanguageManager().getMessage("error.players-only").send(sender);
                        }
                    } else {
                        plugin.getLanguageManager().getMessage(p, "error.no-permission").send(sender);
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
        Player p = sender instanceof Player ? (Player) sender : null;
        plugin.getLanguageManager().getMessage(p, "command.generic.usage")
                .processPlaceholder("%usage%", plugin.getLanguageManager().getMessage(p, "command." + s + ".usage").processPlaceholder("%label%", label).asString()).send(sender);
    }

    private void help(CommandSender sender, String label) {
        Player p = sender instanceof Player ? (Player) sender : null;
        List<String> helpLines = new ArrayList<>();
        helpLines.add(plugin.getLanguageManager().getMessage(p, "command.help.header").asString());

        if (sender instanceof Player && sender.hasPermission("heads.command.open")) {
            helpLines.add(plugin.getLanguageManager().getMessage(p, "command.help.command")
                    .processPlaceholder("%usage%", plugin.getLanguageManager().getMessage(p, "command.open.usage").processPlaceholder("%label%", label).asString())
                    .processPlaceholder("%info%", plugin.getLanguageManager().getMessage(p, "command.open.info").asString()).asString());
        }

        if (sender.hasPermission("heads.command.reload")) {
            helpLines.add(plugin.getLanguageManager().getMessage(p, "command.help.command")
                    .processPlaceholder("%usage%", plugin.getLanguageManager().getMessage(p, "command.reload.usage").processPlaceholder("%label%", label).asString())
                    .processPlaceholder("%info%", plugin.getLanguageManager().getMessage(p, "command.reload.info").asString()).asString());
        }

        helpLines.add(" ");

        sender.sendMessage(helpLines.toArray(new String[0]));
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (command.getName().equals("visibility")) {
            List<String> aliases = new ArrayList<>();

            if (args.length == 1) {
                if (args[0].isEmpty()) {
                    if (sender.hasPermission("heads.command.reload")) aliases.add("reload");
                    if (sender.hasPermission("heads.command.search")) aliases.add("search");
                } else {
                    String arg = args[0].toLowerCase(Locale.ROOT);
                    if ("reload".contains(arg) && sender.hasPermission("heads.command.reload")) aliases.add("reload");
                    if ("search".contains(arg) && sender.hasPermission("heads.command.search")) aliases.add("search");
                }
            } else if (args.length > 1) {
                aliases = Collections.emptyList();
            }

            return aliases;
        }

        return null;
    }
}
