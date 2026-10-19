/*
 * Copyright (c) 2025 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package me.denarydev.heads.command;

import me.denarydev.crystal.paper.item.ItemBuilder;
import me.denarydev.crystal.paper.utils.HeadUtils;
import me.denarydev.crystal.skin.SkinProperty;
import me.denarydev.crystal.skin.SkinProvider;
import me.denarydev.heads.HeadsPlugin;
import me.denarydev.heads.Settings;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class PlayerHeadCommand extends Command {

    public static final String PERMISSION = "heads.playerhead";

    private final HeadsPlugin plugin;

    public PlayerHeadCommand(HeadsPlugin plugin) {
        super("playerhead");
        setAliases(List.of("phead"));
        this.plugin = plugin;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission(PERMISSION)) {
            sender.sendRichMessage(Settings.messages().errors.noPermission);
            return true;
        }

        if (!(sender instanceof final Player player)) {
            sender.sendRichMessage(Settings.messages().errors.playersOnly);
            return true;
        }

        if (args.length > 1) {
            sender.sendRichMessage(Settings.messages().commands.playerhead.usage, Placeholder.unparsed("label", label));
            return true;
        }

        final var name = args.length == 0 ? player.getName() : args[0];

        // SkinsRestorer may hit the network, so the lookup runs off the main thread
        plugin.runAsync(() -> {
            final var skin = findSkin(name);

            player.getScheduler().run(plugin, task -> {
                if (skin.isEmpty()) {
                    player.sendRichMessage(Settings.messages().commands.playerhead.notFound, Placeholder.unparsed("name", name));
                    return;
                }

                final var head = ItemBuilder.fromItem(HeadUtils.createHead(skin.get().value(), skin.get().signature()))
                    .displayNameRich(Settings.messages().commands.playerhead.itemName, Placeholder.unparsed("name", name))
                    .build();

                player.getInventory().addItem(head)
                    .values()
                    .forEach(leftover -> player.getWorld().dropItem(player.getLocation(), leftover));

                player.sendRichMessage(Settings.messages().commands.playerhead.success, Placeholder.unparsed("name", name));
            }, null);
        });

        return true;
    }

    private Optional<SkinProperty> findSkin(String name) {
        // Always look up by name: SkinsRestorer treats the argument as a player name,
        // so passing a UUID would send it to Mojang API and fail
        if (SkinProvider.isInitialized()) {
            try {
                final var skin = SkinProvider.current().playerSkin(name);
                if (skin.isPresent()) return skin;
            } catch (RuntimeException ex) {
                // SkinsRestorer throws unchecked exceptions when Mojang API has no profile for the name
                plugin.logDebug("Skin provider failed for " + name + ": " + ex);
            }
        }

        // Fallback: the textures the server currently shows for this player, if online
        final var online = Bukkit.getPlayerExact(name);
        if (online == null) return Optional.empty();

        return online.getPlayerProfile().getProperties().stream()
            .filter(property -> property.getName().equals("textures"))
            .findFirst()
            .map(property -> new SkinProperty(property.getValue(), property.getSignature()));
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        if (args.length != 1 || !sender.hasPermission(PERMISSION)) return List.of();

        final var arg = args[0].toLowerCase(Locale.ROOT);
        return Bukkit.getOnlinePlayers().stream()
            .map(Player::getName)
            .filter(name -> name.toLowerCase(Locale.ROOT).startsWith(arg))
            .sorted()
            .toList();
    }
}
