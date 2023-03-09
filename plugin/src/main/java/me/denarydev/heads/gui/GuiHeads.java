package me.denarydev.heads.gui;

import me.denarydev.crystal.gui.Button;
import me.denarydev.crystal.gui.Menu;
import me.denarydev.crystal.gui.Template;
import me.denarydev.crystal.locale.LocaleManager;
import me.denarydev.heads.HeadsPlugin;
import me.denarydev.heads.head.Category;
import me.denarydev.heads.impl.EventManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class GuiHeads {

    private final HeadsPlugin plugin = HeadsPlugin.getInstance();
    private final LocaleManager lang = plugin.getLocale();
    private final Player viewer;
    private final Category category;

    public GuiHeads(Player viewer, Template template, Category category) {
        this.viewer = viewer;
        this.category = category;

        showPage(template, 1);
    }

    private void showPage(Template template, int page) {
        int pages = (int) Math.ceil(category.getHeadsCount() / 45.0);

        final var heads = category.getHeads();
        final var menu = Menu.builder(template)
                .title(MiniMessage.miniMessage().deserialize(
                        plugin.getLocale().getMessage("gui", "heads", "title").asString(),
                        Placeholder.unparsed("name", category.getName()),
                        Placeholder.unparsed("count", category.getHeadsCountFormatted())))
                .clickCooldown(200L);

        if (heads.size() > 0) {
            int slot = 0;
            int rows = 6;
            final var headsOnPage = heads.stream().skip((page - 1) * (rows - 1) * 9L).limit((rows - 1) * 9L).toList();
            for (final var head : headsOnPage) {
                final var item = head.getItem();
                menu.button(Button.builder()
                        .slot(slot)
                        .itemStack(item)
                        .action(event -> {
                            if (!EventManager.callPlayerClickHeadEvent(viewer, head, event.getClick())) {
                                viewer.getInventory().addItem(item);
                            }
                        })
                        .build());
                slot++;
            }
        } else {
            menu.button(Button.builder()
                    .slot(22)
                    .type(Material.STRUCTURE_VOID)
                    .displaynameRich(lang.getMessage("gui", "heads", "item", "empty", "name").asString())
                    .build());
        }

        int currentPageSlot = 46;

        if (pages - page == 0 && page - 6 >= 1) {
            menu.button(Button.builder()
                    .slot(currentPageSlot)
                    .type(Material.PAPER)
                    .displaynameRich(lang.getMessage("gui", "heads", "item", "page", "name").asString(),
                            Placeholder.unparsed("page", String.valueOf(page - 6)))
                    .loreRich(lang.getMessage("gui", "heads", "item", "page", "lore").asList(),
                            Placeholder.parsed("id", category.getName()))
                    .action(event -> showPage(template, page - 6))
                    .build());
            currentPageSlot++;
        }

        if (pages - page <= 1 && page - 5 >= 1) {
            menu.button(Button.builder()
                    .slot(currentPageSlot)
                    .type(Material.PAPER)
                    .displaynameRich(lang.getMessage("gui", "heads", "item", "page", "name").asString(),
                            Placeholder.unparsed("page", String.valueOf(page - 5)))
                    .loreRich(lang.getMessage("gui", "heads", "item", "page", "lore").asList(),
                            Placeholder.parsed("id", category.getName()))
                    .action(event -> showPage(template, page - 5))
                    .build());
            currentPageSlot++;
        }

        if (pages - page <= 2 && page - 4 >= 1) {
            menu.button(Button.builder()
                    .slot(currentPageSlot)
                    .type(Material.PAPER)
                    .displaynameRich(lang.getMessage("gui", "heads", "item", "page", "name").asString(),
                            Placeholder.unparsed("page", String.valueOf(page - 4)))
                    .loreRich(lang.getMessage("gui", "heads", "item", "page", "lore").asList(),
                            Placeholder.parsed("id", category.getName()))
                    .action(event -> showPage(template, page - 4))
                    .build());
            currentPageSlot++;
        }

        if (page - 3 >= 1) {
            menu.button(Button.builder()
                    .slot(currentPageSlot)
                    .type(Material.PAPER)
                    .displaynameRich(lang.getMessage("gui", "heads", "item", "page", "name").asString(),
                            Placeholder.unparsed("page", String.valueOf(page - 3)))
                    .loreRich(lang.getMessage("gui", "heads", "item", "page", "lore").asList(),
                            Placeholder.parsed("id", category.getName()))
                    .action(event -> showPage(template, page - 3))
                    .build());
            currentPageSlot++;
        }

        if (page - 2 >= 1) {
            menu.button(Button.builder()
                    .slot(currentPageSlot)
                    .type(Material.PAPER)
                    .displaynameRich(lang.getMessage("gui", "heads", "item", "page", "name").asString(),
                            Placeholder.unparsed("page", String.valueOf(page - 2)))
                    .loreRich(lang.getMessage("gui", "heads", "item", "page", "lore").asList(),
                            Placeholder.parsed("id", category.getName()))
                    .action(event -> showPage(template, page - 2))
                    .build());
            currentPageSlot++;
        }

        if (page - 1 >= 1) {
            menu.button(Button.builder()
                    .slot(currentPageSlot)
                    .type(Material.PAPER)
                    .displaynameRich(lang.getMessage("gui", "heads", "item", "page", "name").asString(),
                            Placeholder.unparsed("page", String.valueOf(page - 1)))
                    .loreRich(lang.getMessage("gui", "heads", "item", "page", "lore").asList(),
                            Placeholder.parsed("id", category.getName()))
                    .action(event -> showPage(template, page - 1))
                    .build());
            currentPageSlot++;
        }

        menu.button(Button.builder()
                .slot(currentPageSlot)
                .type(Material.MAP)
                .displaynameRich(lang.getMessage("gui", "heads", "item", "page", "name").asString(),
                        Placeholder.unparsed("page", String.valueOf(page)))
                .loreRich(lang.getMessage("gui", "heads", "item", "page", "lore").asList(),
                        Placeholder.parsed("id", category.getName()))
                .build());
        currentPageSlot++;

        if (page + 1 <= pages) {
            menu.button(Button.builder()
                    .slot(currentPageSlot)
                    .type(Material.PAPER)
                    .displaynameRich(lang.getMessage("gui", "heads", "item", "page", "name").asString(),
                            Placeholder.unparsed("page", String.valueOf(page + 1)))
                    .loreRich(lang.getMessage("gui", "heads", "item", "page", "lore").asList(),
                            Placeholder.parsed("id", category.getName()))
                    .action(event -> showPage(template, page + 1))
                    .build());
            currentPageSlot++;
        }

        if (page + 2 <= pages) {
            menu.button(Button.builder()
                    .slot(currentPageSlot)
                    .type(Material.PAPER)
                    .displaynameRich(lang.getMessage("gui", "heads", "item", "page", "name").asString(),
                            Placeholder.unparsed("page", String.valueOf(page + 2)))
                    .loreRich(lang.getMessage("gui", "heads", "item", "page", "lore").asList(),
                            Placeholder.parsed("id", category.getName()))
                    .action(event -> showPage(template, page + 2))
                    .build());
            currentPageSlot++;
        }

        if (page + 3 <= pages) {
            menu.button(Button.builder()
                    .slot(currentPageSlot)
                    .type(Material.PAPER)
                    .displaynameRich(lang.getMessage("gui", "heads", "item", "page", "name").asString(),
                            Placeholder.unparsed("page", String.valueOf(page + 3)))
                    .loreRich(lang.getMessage("gui", "heads", "item", "page", "lore").asList(),
                            Placeholder.parsed("id", category.getName()))
                    .action(event -> showPage(template, page + 3))
                    .build());
            currentPageSlot++;
        }

        if (page + 4 <= pages && currentPageSlot < 53) {
            menu.button(Button.builder()
                    .slot(currentPageSlot)
                    .type(Material.PAPER)
                    .displaynameRich(lang.getMessage("gui", "heads", "item", "page", "name").asString(),
                            Placeholder.unparsed("page", String.valueOf(page + 4)))
                    .loreRich(lang.getMessage("gui", "heads", "item", "page", "lore").asList(),
                            Placeholder.parsed("id", category.getName()))
                    .action(event -> showPage(template, page + 4))
                    .build());
            currentPageSlot++;
        }

        if (page + 5 <= pages && currentPageSlot < 53) {
            menu.button(Button.builder()
                    .slot(currentPageSlot)
                    .type(Material.PAPER)
                    .displaynameRich(lang.getMessage("gui", "heads", "item", "page", "name").asString(),
                            Placeholder.unparsed("page", String.valueOf(page + 5)))
                    .loreRich(lang.getMessage("gui", "heads", "item", "page", "lore").asList(),
                            Placeholder.parsed("id", category.getName()))
                    .action(event -> showPage(template, page + 5))
                    .build());
            currentPageSlot++;
        }

        if (page + 6 <= pages && currentPageSlot < 53) {
            menu.button(Button.builder()
                    .slot(currentPageSlot)
                    .type(Material.PAPER)
                    .displaynameRich(lang.getMessage("gui", "heads", "item", "page", "name").asString(),
                            Placeholder.unparsed("page", String.valueOf(page + 6)))
                    .loreRich(lang.getMessage("gui", "heads", "item", "page", "lore").asList(),
                            Placeholder.parsed("id", category.getName()))
                    .action(event -> showPage(template, page + 6))
                    .build());
        }

        menu.clickAction(event -> plugin.getGuiManager().openOverview(viewer), 45);
        //menu.clickAction(event -> plugin.getHeadDBManager().showSearchChatPrompt(viewer, null, event.isRightClick()), 53);

        menu.build().open(viewer);
    }
}
