/*
 * Copyright (c) 2022 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package io.sapphiremc.heads.gui;

import com.songoda.core.compatibility.CompatibleMaterial;
import com.songoda.core.gui.Gui;
import com.songoda.core.gui.GuiUtils;
import com.songoda.core.utils.ItemUtils;
import io.sapphiremc.heads.HeadsPlugin;
import io.sapphiremc.heads.hdb.Category;
import io.sapphiremc.heads.hdb.Head;
import io.sapphiremc.heads.utils.Formatter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.List;

public class GuiHeads extends Gui {

    private final HeadsPlugin plugin = HeadsPlugin.getInstance();
    private final Player player;
    private final Type guiType;
    @Nullable
    private final Category category;
    private final List<Head> heads;

    public GuiHeads(Player player, Type guiType, @Nullable Category category, List<Head> heads) {
        this.player = player;
        this.guiType = guiType;
        this.category = category;
        this.heads = heads;

        this.setDefaultItem(null);
        this.setRows(6);
        this.setOnPage((event -> showPage()));
        showPage();
    }

    private void updateTitle() {
        if (guiType.equals(Type.CATEGORY) && category != null) {
            this.setTitle(plugin.getLanguageManager().getMessage(player, "gui.heads.title.category").processPlaceholder("%name%", category.getName(player))
                    .processPlaceholder("%count%", category.getHeadsCountFormatted()).asString());
        } else if (guiType.equals(Type.SEARCH)) {
            this.setTitle(plugin.getLanguageManager().getMessage(player, "gui.heads.title.search").processPlaceholder("%count%", Formatter.formatCount(heads.size())).asString());
        } else if (guiType.equals(Type.RECENT)) {
            this.setTitle(plugin.getLanguageManager().getMessage(player, "gui.heads.title.recent").processPlaceholder("%count%", Formatter.formatCount(heads.size())).asString());
        } else if (guiType.equals(Type.FAVORITES)) {
            this.setTitle(plugin.getLanguageManager().getMessage(player, "gui.heads.title.favorites").processPlaceholder("%count%", Formatter.formatCount(heads.size())).asString());
        }
    }

    private void showPage() {
        updateTitle();
        this.setPages((int) Math.ceil(heads.size() / 45.0));
        List<Head> headsOnPage = heads.stream().skip((page - 1) * (rows - 1) * 9L).limit((rows - 1) * 9L).toList();

        for (int i = 0; i < 45; i++) {
            clearActions(i);
            setItem(i, null);
        }

        if (heads.size() > 0) {
            int slot = 0;
            for (Head head : headsOnPage) {
                ItemStack item = ItemUtils.getCustomHead(head.getBase64());
                setButton(slot, GuiUtils.createButtonItem(item,
                                plugin.getLanguageManager().getMessage(player, "gui.heads.item.head.name").processPlaceholder("%name%", head.getName()).asString(),
                                plugin.getLanguageManager().getMessage(player, "gui.heads.item.head.lore")
                                        .processPlaceholder("%category%", head.getCategory().getName(player))
                                        .processPlaceholder("%tags%", head.getTagsString())
                                        .processPlaceholder("%id%", String.valueOf(head.getId()))
                                        .asList()
                        ),
                        (event) ->
                        player.getInventory().addItem(GuiUtils.createButtonItem(item, ChatColor.BLUE + head.getName())));
                slot++;
            }
        } else {
            setButton(22, GuiUtils.createButtonItem(CompatibleMaterial.RED_STAINED_GLASS_PANE, plugin.getLanguageManager().getMessage(player, "gui.heads.item.empty.name").asString()), (event -> {}));
        }

        setButton(45, GuiUtils.createButtonItem(CompatibleMaterial.BOOK, plugin.getLanguageManager().getMessage(player, "gui.common.item.back.name").asString()), (event -> plugin.showGUI(player, new GUIOverview(player))));

        int currentPageSlot = 46;

        if (pages - page == 0 && page - 6 >= 1) {
            setButton(currentPageSlot, GuiUtils.createButtonItem(CompatibleMaterial.PAPER, page - 6, plugin.getLanguageManager().getMessage(player, "gui.heads.item.page.name")
                            .processPlaceholder("%page%", String.valueOf(page - 6)).asString(), getPagesLore()), (event -> changePage(-6)));
            currentPageSlot++;
        } else {
            clearActions(currentPageSlot);
            setItem(currentPageSlot, null);
        }

        if (pages - page <= 1 && page - 5 >= 1) {
            setButton(currentPageSlot, GuiUtils.createButtonItem(CompatibleMaterial.PAPER, page - 5, plugin.getLanguageManager().getMessage(player, "gui.heads.item.page.name")
                            .processPlaceholder("%page%", String.valueOf(page - 5)).asString(), getPagesLore()), (event -> changePage(-5)));
            currentPageSlot++;
        } else {
            clearActions(currentPageSlot);
            setItem(currentPageSlot, null);
        }

        if (pages - page <= 2 && page - 4 >= 1) {
            setButton(currentPageSlot, GuiUtils.createButtonItem(CompatibleMaterial.PAPER, page - 4, plugin.getLanguageManager().getMessage(player, "gui.heads.item.page.name")
                            .processPlaceholder("%page%", String.valueOf(page - 4)).asString(), getPagesLore()), (event -> changePage(-4)));
            currentPageSlot++;
        } else {
            clearActions(currentPageSlot);
            setItem(currentPageSlot, null);
        }

        if (page - 3 >= 1) {
            setButton(currentPageSlot, GuiUtils.createButtonItem(CompatibleMaterial.PAPER, page - 3, plugin.getLanguageManager().getMessage(player, "gui.heads.item.page.name")
                            .processPlaceholder("%page%", String.valueOf(page - 3)).asString(), getPagesLore()), (event -> changePage(-3)));
            currentPageSlot++;
        } else {
            clearActions(currentPageSlot);
            setItem(currentPageSlot, null);
        }

        if (page - 2 >= 1) {
            setButton(currentPageSlot, GuiUtils.createButtonItem(CompatibleMaterial.PAPER, page - 2, plugin.getLanguageManager().getMessage(player, "gui.heads.item.page.name")
                            .processPlaceholder("%page%", String.valueOf(page - 2)).asString(), getPagesLore()), (event -> changePage(-2)));
            currentPageSlot++;
        } else {
            clearActions(currentPageSlot);
            setItem(currentPageSlot, null);
        }

        if (page - 1 >= 1) {
            setButton(currentPageSlot, GuiUtils.createButtonItem(CompatibleMaterial.PAPER, page - 1, plugin.getLanguageManager().getMessage(player, "gui.heads.item.page.name")
                            .processPlaceholder("%page%", String.valueOf(page - 1)).asString(), getPagesLore()), (event -> changePage(-1)));
            currentPageSlot++;
        } else {
            clearActions(currentPageSlot);
            setItem(currentPageSlot, null);
        }

        setButton(currentPageSlot, GuiUtils.createButtonItem(CompatibleMaterial.MAP, page, plugin.getLanguageManager().getMessage(player, "gui.heads.item.page.name")
                        .processPlaceholder("%page%", String.valueOf(page)).asString(), getPagesLore()), (event -> {}));
        currentPageSlot++;

        if (page + 1 <= pages) {
            setButton(currentPageSlot, GuiUtils.createButtonItem(CompatibleMaterial.PAPER, page + 1, plugin.getLanguageManager().getMessage(player, "gui.heads.item.page.name")
                            .processPlaceholder("%page%", String.valueOf(page + 1)).asString(), getPagesLore()), (event -> changePage(+1)));
            currentPageSlot++;
        } else {
            clearActions(currentPageSlot);
            setItem(currentPageSlot, null);
        }

        if (page + 2 <= pages) {
            setButton(currentPageSlot, GuiUtils.createButtonItem(CompatibleMaterial.PAPER, page + 2, plugin.getLanguageManager().getMessage(player, "gui.heads.item.page.name")
                            .processPlaceholder("%page%", String.valueOf(page + 2)).asString(), getPagesLore()), (event -> changePage(+2)));
            currentPageSlot++;
        } else {
            clearActions(currentPageSlot);
            setItem(currentPageSlot, null);
        }

        if (page + 3 <= pages) {
            setButton(currentPageSlot, GuiUtils.createButtonItem(CompatibleMaterial.PAPER, page + 3, plugin.getLanguageManager().getMessage(player, "gui.heads.item.page.name")
                            .processPlaceholder("%page%", String.valueOf(page + 3)).asString(), getPagesLore()), (event -> changePage(+3)));
            currentPageSlot++;
        } else {
            clearActions(currentPageSlot);
            setItem(currentPageSlot, null);
        }

        if (page + 4 <= pages && currentPageSlot < 53) {
            setButton(currentPageSlot, GuiUtils.createButtonItem(CompatibleMaterial.PAPER, page + 4, plugin.getLanguageManager().getMessage(player, "gui.heads.item.page.name")
                            .processPlaceholder("%page%", String.valueOf(page + 4)).asString(), getPagesLore()), (event -> changePage(+4)));
            currentPageSlot++;
        } else {
            clearActions(currentPageSlot);
            setItem(currentPageSlot, null);
        }

        if (page + 5 <= pages && currentPageSlot < 53) {
            setButton(currentPageSlot, GuiUtils.createButtonItem(CompatibleMaterial.PAPER, page + 5, plugin.getLanguageManager().getMessage(player, "gui.heads.item.page.name")
                            .processPlaceholder("%page%", String.valueOf(page + 5)).asString(), getPagesLore()), (event -> changePage(+5)));
            currentPageSlot++;
        } else {
            clearActions(currentPageSlot);
            setItem(currentPageSlot, null);
        }

        if (page + 6 <= pages && currentPageSlot < 53) {
            setButton(currentPageSlot, GuiUtils.createButtonItem(CompatibleMaterial.PAPER, page + 6, plugin.getLanguageManager().getMessage(player, "gui.heads.item.page.name")
                            .processPlaceholder("%page%", String.valueOf(page + 6)).asString(), getPagesLore()), (event -> changePage(+6)));
        } else {
            clearActions(currentPageSlot);
            setItem(currentPageSlot, null);
        }

        setButton(53, GuiUtils.createButtonItem(CompatibleMaterial.COMPASS, plugin.getLanguageManager().getMessage(player, "gui.common.item.search.name").asString(),
                plugin.getLanguageManager().getMessage(player, "gui.common.item.search.lore").asList()), (event -> {
            if (event.clickType.isLeftClick()) {
                plugin.getHeadDBManager().showSearchChatPrompt(player, category, false);
            } else if (event.clickType.isRightClick()) {
                plugin.getHeadDBManager().showSearchChatPrompt(player, category, true);
            }
        }));
    }

    private List<String> getPagesLore() {
        if (guiType.equals(Type.CATEGORY) && category != null) {
            return plugin.getLanguageManager().getMessage(player, "gui.heads.item.page.lore.category").processPlaceholder("%id%", category.getName(player)).asList();
        } else if (guiType.equals(Type.SEARCH)) {
            return plugin.getLanguageManager().getMessage(player, "gui.heads.item.page.lore.search").asList();
        } else if (guiType.equals(Type.RECENT)) {
            return plugin.getLanguageManager().getMessage(player, "gui.heads.item.page.lore.recent").asList();
        } else if (guiType.equals(Type.FAVORITES)) {
            return plugin.getLanguageManager().getMessage(player, "gui.heads.item.page.lore.favorites").asList();
        }
        return null;
    }

    public enum Type {
        CATEGORY, SEARCH, RECENT, FAVORITES
    }
}
