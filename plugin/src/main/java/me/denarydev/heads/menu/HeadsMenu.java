/*
 * Copyright (c) 2023 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package me.denarydev.heads.menu;

import me.denarydev.crystal.gui.Menu;
import me.denarydev.crystal.gui.Template;
import me.denarydev.crystal.utils.ItemUtils;
import me.denarydev.heads.HeadsPlugin;
import me.denarydev.heads.Settings;
import me.denarydev.heads.head.Category;
import me.denarydev.heads.api.EventManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;

public class HeadsMenu {

    private final HeadsPlugin plugin = HeadsPlugin.getInstance();
    private final Player viewer;
    private final Category category;

    public HeadsMenu(Player viewer, Template template, Category category) {
        this.viewer = viewer;
        this.category = category;

        showPage(template, 1);
    }

    private void showPage(Template template, int page) {
        int pages = (int) Math.ceil(category.getHeadsCount() / 45.0);

        final var heads = category.getHeads();
        final var menu = Menu.builder(template)
                .title(MiniMessage.miniMessage().deserialize(
                        Settings.menus().heads.title,
                        Placeholder.unparsed("name", category.getName()),
                        Placeholder.unparsed("count", category.getHeadsCountFormatted())))
                .clickCooldown(200L);

        if (!heads.isEmpty()) {
            int slot = 0;
            int rows = 6;
            final var headsOnPage = heads.stream().skip((page - 1) * (rows - 1) * 9L).limit((rows - 1) * 9L).toList();
            for (final var head : headsOnPage) {
                final var item = head.itemStack();
                menu.item(ItemUtils.itemBuilder()
                                .itemStack(item)
                                .build(),
                        event -> {
                            if (!EventManager.callPlayerClickHeadEvent(viewer, head, event.getClick())) {
                                viewer.getInventory().addItem(item);
                            }
                        }, slot);
                slot++;
            }
        } else {
            menu.item(Settings.menus().heads.items.empty, 22);
        }

        int currentPageSlot = 46;

        if (pages - page == 0 && page - 6 >= 1) {
            menu.item(ItemUtils.itemBuilder()
                    .type(Settings.menus().heads.items.pageUnselected.type)
                    .displayNameRich(Settings.menus().heads.items.pageUnselected.name,
                            Placeholder.unparsed("page", String.valueOf(page - 6)))
                    .loreRich(Settings.menus().heads.items.pageUnselected.lore,
                            Placeholder.parsed("id", category.getName()))
                    .build(), event -> showPage(template, page - 6), currentPageSlot);
            currentPageSlot++;
        }

        if (pages - page <= 1 && page - 5 >= 1) {
            menu.item(ItemUtils.itemBuilder()
                    .type(Settings.menus().heads.items.pageUnselected.type)
                    .displayNameRich(Settings.menus().heads.items.pageUnselected.name,
                            Placeholder.unparsed("page", String.valueOf(page - 5)))
                    .loreRich(Settings.menus().heads.items.pageUnselected.lore,
                            Placeholder.parsed("id", category.getName()))
                    .build(), event -> showPage(template, page - 5), currentPageSlot);
            currentPageSlot++;
        }

        if (pages - page <= 2 && page - 4 >= 1) {
            menu.item(ItemUtils.itemBuilder()
                    .type(Settings.menus().heads.items.pageUnselected.type)
                    .displayNameRich(Settings.menus().heads.items.pageUnselected.name,
                            Placeholder.unparsed("page", String.valueOf(page - 4)))
                    .loreRich(Settings.menus().heads.items.pageUnselected.lore,
                            Placeholder.parsed("id", category.getName()))
                    .build(), event -> showPage(template, page - 4), currentPageSlot);
            currentPageSlot++;
        }

        if (page - 3 >= 1) {
            menu.item(ItemUtils.itemBuilder()
                    .type(Settings.menus().heads.items.pageUnselected.type)
                    .displayNameRich(Settings.menus().heads.items.pageUnselected.name,
                            Placeholder.unparsed("page", String.valueOf(page - 3)))
                    .loreRich(Settings.menus().heads.items.pageUnselected.lore,
                            Placeholder.parsed("id", category.getName()))
                    .build(), event -> showPage(template, page - 3), currentPageSlot);
            currentPageSlot++;
        }

        if (page - 2 >= 1) {
            menu.item(ItemUtils.itemBuilder()
                    .type(Settings.menus().heads.items.pageUnselected.type)
                    .displayNameRich(Settings.menus().heads.items.pageUnselected.name,
                            Placeholder.unparsed("page", String.valueOf(page - 2)))
                    .loreRich(Settings.menus().heads.items.pageUnselected.lore,
                            Placeholder.parsed("id", category.getName()))
                    .build(), event -> showPage(template, page - 2), currentPageSlot);
            currentPageSlot++;
        }

        if (page - 1 >= 1) {
            menu.item(ItemUtils.itemBuilder()
                    .type(Settings.menus().heads.items.pageUnselected.type)
                    .displayNameRich(Settings.menus().heads.items.pageUnselected.name,
                            Placeholder.unparsed("page", String.valueOf(page - 1)))
                    .loreRich(Settings.menus().heads.items.pageUnselected.lore,
                            Placeholder.parsed("id", category.getName()))
                    .build(), event -> showPage(template, page - 1), currentPageSlot);
            currentPageSlot++;
        }

        menu.item(ItemUtils.itemBuilder()
                .type(Settings.menus().heads.items.pageSelected.type)
                .displayNameRich(Settings.menus().heads.items.pageSelected.name,
                        Placeholder.unparsed("page", String.valueOf(page)))
                .loreRich(Settings.menus().heads.items.pageSelected.lore,
                        Placeholder.parsed("id", category.getName()))
                .build(), currentPageSlot);
        currentPageSlot++;

        if (page + 1 <= pages) {
            menu.item(ItemUtils.itemBuilder()
                    .type(Settings.menus().heads.items.pageUnselected.type)
                    .displayNameRich(Settings.menus().heads.items.pageUnselected.name,
                            Placeholder.unparsed("page", String.valueOf(page + 1)))
                    .loreRich(Settings.menus().heads.items.pageUnselected.lore,
                            Placeholder.parsed("id", category.getName()))
                    .build(), event -> showPage(template, page + 1), currentPageSlot);
            currentPageSlot++;
        }

        if (page + 2 <= pages) {
            menu.item(ItemUtils.itemBuilder()
                    .type(Settings.menus().heads.items.pageUnselected.type)
                    .displayNameRich(Settings.menus().heads.items.pageUnselected.name,
                            Placeholder.unparsed("page", String.valueOf(page + 2)))
                    .loreRich(Settings.menus().heads.items.pageUnselected.lore,
                            Placeholder.parsed("id", category.getName()))
                    .build(), event -> showPage(template, page + 2), currentPageSlot);
            currentPageSlot++;
        }

        if (page + 3 <= pages) {
            menu.item(ItemUtils.itemBuilder()
                    .type(Settings.menus().heads.items.pageUnselected.type)
                    .displayNameRich(Settings.menus().heads.items.pageUnselected.name,
                            Placeholder.unparsed("page", String.valueOf(page + 3)))
                    .loreRich(Settings.menus().heads.items.pageUnselected.lore,
                            Placeholder.parsed("id", category.getName()))
                    .build(), event -> showPage(template, page + 3), currentPageSlot);
            currentPageSlot++;
        }

        if (page + 4 <= pages && currentPageSlot < 53) {
            menu.item(ItemUtils.itemBuilder()
                    .type(Settings.menus().heads.items.pageUnselected.type)
                    .displayNameRich(Settings.menus().heads.items.pageUnselected.name,
                            Placeholder.unparsed("page", String.valueOf(page + 4)))
                    .loreRich(Settings.menus().heads.items.pageUnselected.lore,
                            Placeholder.parsed("id", category.getName()))
                    .build(), event -> showPage(template, page + 4), currentPageSlot);
            currentPageSlot++;
        }

        if (page + 5 <= pages && currentPageSlot < 53) {
            menu.item(ItemUtils.itemBuilder()
                    .type(Settings.menus().heads.items.pageUnselected.type)
                    .displayNameRich(Settings.menus().heads.items.pageUnselected.name,
                            Placeholder.unparsed("page", String.valueOf(page + 5)))
                    .loreRich(Settings.menus().heads.items.pageUnselected.lore,
                            Placeholder.parsed("id", category.getName()))
                    .build(), event -> showPage(template, page + 5), currentPageSlot);
            currentPageSlot++;
        }

        if (page + 6 <= pages && currentPageSlot < 53) {
            menu.item(ItemUtils.itemBuilder()
                    .type(Settings.menus().heads.items.pageUnselected.type)
                    .displayNameRich(Settings.menus().heads.items.pageUnselected.name,
                            Placeholder.unparsed("page", String.valueOf(page + 6)))
                    .loreRich(Settings.menus().heads.items.pageUnselected.lore,
                            Placeholder.parsed("id", category.getName()))
                    .build(), event -> showPage(template, page + 6), currentPageSlot);
        }

        menu.action(event -> plugin.getMenuManager().openOverview(viewer), 45);
        //menu.clickAction(event -> plugin.getHeadDBManager().showSearchChatPrompt(viewer, null, event.isRightClick()), 53);

        menu.build().open(viewer);
    }
}
