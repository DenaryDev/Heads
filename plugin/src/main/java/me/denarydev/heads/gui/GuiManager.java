package me.denarydev.heads.gui;

import me.denarydev.crystal.gui.Button;
import me.denarydev.crystal.gui.InventoryListener;
import me.denarydev.crystal.gui.Menu;
import me.denarydev.crystal.gui.Template;
import me.denarydev.crystal.utils.NumberUtils;
import me.denarydev.heads.HeadsPlugin;
import me.denarydev.heads.Settings;
import me.denarydev.heads.api.head.CategoryID;
import me.denarydev.heads.head.Category;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public final class GuiManager {

    private final HeadsPlugin plugin;

    private Template overview;
    private Template heads;

    public GuiManager(HeadsPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(new InventoryListener(), plugin);
    }

    public void loadTemplates() {
        final var lang = HeadsPlugin.getInstance().getLocale();
        overview = Template.builder()
                .titleRich(plugin.getLocale().getMessage("gui", "overview", "title").asString(),
                        Placeholder.unparsed("count", NumberUtils.formatWithSuffix(plugin.getHeadDBManager().getHeadsLoaded())))
                .rows(6)
                .button(Button.builder()
                        .slots(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53)
                        .type(Material.GRAY_STAINED_GLASS_PANE)
                        .displayname(Component.empty())
                        .build())
                .button(Button.builder()
                        .slot(43)
                        .type(Material.COMPASS)
                        .displaynameRich(lang.getMessage("gui", "common", "item", "search", "name").asString())
                        .loreRich(lang.getMessage("gui", "common", "item", "search", "lore").asList())
                        .build())
                .build();
        heads = Template.builder()
                .rows(6)
                .button(Button.builder()
                        .slot(45)
                        .type(Material.BOOK)
                        .displaynameRich(lang.getMessage("gui", "common", "item", "back", "name").asString())
                        .build())
                .button(Button.builder()
                        .slot(53)
                        .type(Material.COMPASS)
                        .displaynameRich(lang.getMessage("gui", "common", "item", "search", "name").asString())
                        .loreRich(lang.getMessage("gui", "common", "item", "search", "lore").asList())
                        .build())
                .build();
    }

    public void openOverview(Player viewer) {
        final var lang = HeadsPlugin.getInstance().getLocale();
        final var menu = Menu.builder(overview)
                .clickCooldown(200L);

        var slot = 10;
        for (final var category : plugin.getHeadDBManager().getCategories().values()) {
            final var categoryId = category.getId();
            if (categoryId == CategoryID.SEARCH || categoryId == CategoryID.FAVORITES) continue;
            if (Settings.main().perCategoryPermissions && !viewer.hasPermission("heads.category." + categoryId.toLower()))
                continue;

            menu.button(Button.builder()
                    .slot(categoryId == CategoryID.RECENT ? 37 : slot)
                    .itemStack(category.getMenuItem())
                    .displaynameRich(lang.getMessage("gui", "overview", "item", "category", "name").asString(),
                            Placeholder.unparsed("name", category.getName()))
                    .loreRich(lang.getMessage("gui", "overview", "item", "category", "lore").asList(),
                            Placeholder.unparsed("count", category.getHeadsCountFormatted()))
                    .action(event -> openHeads(viewer, category))
                    .build());

            if (categoryId != CategoryID.RECENT) slot++;
            if (slot == 17) slot = 19;
            if (slot == 26) slot = 28;
            if (slot == 35) break;
        }

        //menu.clickAction(event -> plugin.getHeadDBManager().showSearchChatPrompt(viewer, null, event.isRightClick()), 43);

        menu.build().open(viewer);
    }

    public void openHeads(Player viewer, Category category) {
        plugin.runSync(() -> new GuiHeads(viewer, heads, category));
    }

    public void closeAll() {
        for (final var p : Bukkit.getOnlinePlayers()) {
            if (p.getInventory().getHolder() instanceof Menu) {
                p.closeInventory();
            }
        }
    }
}
