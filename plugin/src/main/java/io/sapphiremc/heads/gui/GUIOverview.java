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
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;

public class GUIOverview extends Gui {

    private final HeadsPlugin plugin;
    private final Player player;

    public GUIOverview(Player player) {
        this.plugin = HeadsPlugin.getInstance();
        this.player = player;

        this.setDefaultItem(null);
        this.setRows(6);
        this.setTitle(plugin.getLanguageManager().getMessage(player, "gui.overview.title").processPlaceholder("%count%", Formatter.formatCount(plugin.getHeadDBManager().getHeadsLoaded())).asString());
        this.setOnPage(event -> showPage());
        showPage();
    }

    private void showPage() {
        ItemStack background = GuiUtils.getBorderItem(CompatibleMaterial.GRAY_STAINED_GLASS_PANE);

        int[] slots = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53};
        for (int slot : slots) {
            setItem(slot, background);
        }

        int slot = 10;
        for (Category category : plugin.getHeadDBManager().getCategories()) {
            if (plugin.getConfiguration().getBoolean("per-category-permission", false) && !player.hasPermission("heads.category." + category.getId().toLower())) continue;

            setButton(slot, GuiUtils.createButtonItem(category.getMenuItem(),
                    plugin.getLanguageManager().getMessage(player, "gui.overview.item.category.name")
                            .processPlaceholder("%name%", category.getName(player)).asString(),
                    plugin.getLanguageManager().getMessage(player, "gui.overview.item.category.lore")
                            .processPlaceholder("%count%", category.getHeadsCountFormatted()).asList()),
                    (event -> plugin.showGUI(player, new GuiHeads(player, GuiHeads.Type.CATEGORY, category, category.getHeads()))));
            slot++;
            if (slot == 17) slot = 19;
            if (slot == 26) slot = 28;
            if (slot == 35) slot = 38;
            if (slot > 43) break;
        }
        setButton(37, GuiUtils.createButtonItem(ItemUtils.getCustomHead(getRandomHead(plugin.getHeadDBManager().getRecentlyHeads()).getBase64()), plugin.getLanguageManager().getMessage("heads.category.recent").asString()), (event ->
                plugin.showGUI(player, new GuiHeads(player, GuiHeads.Type.RECENT, null, plugin.getHeadDBManager().getRecentlyHeads())))
        );

        setButton(43, GuiUtils.createButtonItem(CompatibleMaterial.COMPASS, plugin.getLanguageManager().getMessage(player, "gui.common.item.search.name").asString(),
                plugin.getLanguageManager().getMessage(player, "gui.common.item.search.lore").asList()), (event -> {
            if (event.clickType.isLeftClick()) {
                plugin.getHeadDBManager().showSearchChatPrompt(player, null, false);
            } else if (event.clickType.isRightClick()) {
                plugin.getHeadDBManager().showSearchChatPrompt(player, null, true);
            }
        }));
    }

    private Head getRandomHead(List<Head> list) {
        Random random = new Random();
        return list.get(random.nextInt(0, list.size() - 1));
    }
}
