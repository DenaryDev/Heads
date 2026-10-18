/*
 * Copyright (c) 2025 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package me.denarydev.heads.menu;

import me.denarydev.crystal.paper.gui.Menu;
import me.denarydev.crystal.paper.gui.Template;
import me.denarydev.crystal.paper.item.ItemBuilder;
import me.denarydev.crystal.utils.NumberUtils;
import me.denarydev.heads.HeadsPlugin;
import me.denarydev.heads.Settings;
import me.denarydev.heads.api.head.CategoryID;
import me.denarydev.heads.head.Category;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public final class MenuManager {

    private final HeadsPlugin plugin;

    private Template overview;
    private Template heads;

    public MenuManager(HeadsPlugin plugin) {
        this.plugin = plugin;
    }

    public void loadTemplates() {
        overview = Template.simpleBuilder()
            .titleRich(Settings.menus().overview.title,
                Placeholder.unparsed("count", NumberUtils.formatNumber(plugin.getHeadDBManager().getHeadsLoaded())))
            .size(54)
            .item(Settings.menus().common.items.background,
                0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53)
            .item(Settings.menus().common.items.search, 43)
            .build();
        heads = Template.simpleBuilder()
            .size(54)
            .item(Settings.menus().common.items.back, 45)
            .item(Settings.menus().common.items.search, 53)
            .build();
    }

    public void openOverview(Player viewer) {
        final var menu = Menu.builder(overview)
            .cooldown(200L);

        var slot = 10;
        final var nameAndLore = Settings.menus().overview.items.category;
        for (final var category : plugin.getHeadDBManager().getCategories().values()) {
            final var categoryId = category.getId();
            if (categoryId == CategoryID.SEARCH || categoryId == CategoryID.FAVORITES // Эти категории отображаются отдельно
                || (Settings.main().perCategoryPermissions && !viewer.hasPermission("heads.category." + categoryId.toLower()))
                || (category.getHeadsCount() < 1 && !Settings.main().showEmptyCategories)
            ) continue;

            final var lore = new ArrayList<String>();
            if (category.getDescription() != null)
                lore.addAll(category.getDescription());
            lore.addAll(nameAndLore.lore);

            menu.item(ItemBuilder.fromItem(category.getMenuItem())
                .displayNameRich(nameAndLore.name, Placeholder.unparsed("name", category.getName()))
                .loreRich(lore, Placeholder.unparsed("count", category.getHeadsCountFormatted()))
                .build(), event -> openHeads(viewer, category), categoryId == CategoryID.RECENT ? 37 : slot);

            if (categoryId != CategoryID.RECENT) slot++;
            if (slot == 17) slot = 19;
            if (slot == 26) slot = 28;
            if (slot == 35) break;
        }

        //menu.clickAction(event -> plugin.getHeadDBManager().showSearchChatPrompt(viewer, null, event.isRightClick()), 43);

        menu.build().show(viewer);
    }

    public void openHeads(Player viewer, Category category) {
        plugin.sync(() -> new HeadsMenu(viewer, heads, category));
    }

    public void closeAll() {
        for (final var p : Bukkit.getOnlinePlayers()) {
            if (p.getInventory().getHolder() instanceof Menu) {
                p.closeInventory();
            }
        }
    }
}
