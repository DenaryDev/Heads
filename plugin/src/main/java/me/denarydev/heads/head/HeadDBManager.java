/*
 * Copyright (c) 2022 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package me.denarydev.heads.head;

import lombok.Getter;
import me.denarydev.crystal.locale.Pair;
import me.denarydev.crystal.utils.ItemUtils;
import me.denarydev.heads.HeadsPlugin;
import me.denarydev.heads.Settings;
import me.denarydev.heads.api.events.PlayerSearchHeadsEvent;
import me.denarydev.heads.api.head.CategoryID;
import me.denarydev.heads.api.head.HeadsManager;
import me.denarydev.heads.impl.EventManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;

public class HeadDBManager implements HeadsManager {

    private final HeadsPlugin plugin;
    @Getter
    private final Map<CategoryID, Category> categories = new LinkedHashMap<>();

    @Getter
    private boolean databaseLoaded = false;
    @Getter
    private int headsLoaded;
    @Getter
    private int categoriesLoaded;

    public HeadDBManager(HeadsPlugin plugin) {
        this.plugin = plugin;
    }

    public void loadDatabase() {
        loadDatabase(null);
    }

    public void loadDatabase(Consumer<Long> callback) {
        plugin.logDebug("Loading database...");
        final long startTime = System.currentTimeMillis();
        databaseLoaded = false;
        categories.clear();
        headsLoaded = 0;
        categoriesLoaded = 0;

        final Map<String, List<Head>> readContent = new TreeMap<>();
        try {
            plugin.logDebug("Fetching heads from website");
            final var url = new URL("https://minecraft-heads.4lima.de/csv/2022-02-25-ZgFDreHnLiGvHdf3RFfgg/Custom-Head-DB.csv");
            final var connection = url.openConnection();

            try (final var reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    final String[] values = line.split(";");
                    final String category = values[0];
                    final var head = new Head(CategoryID.getCategoryID(values[0]), Integer.parseInt(values[1]),
                            values[2].replace("\"", ""), values[3], Integer.parseInt(values[4]) == 1,
                            values.length == 5 ? Collections.emptyList() : List.of(values[5].replace("\"", "").toLowerCase().split("\\|"))
                    );

                    final List<Head> heads = new ArrayList<>();
                    if (readContent.containsKey(category))
                        heads.addAll(readContent.get(category));
                    heads.add(head);
                    readContent.put(category, heads);
                    headsLoaded++;
                }
            }
        } catch (IOException ex) {
            plugin.getLogger().severe("Error fetching heads from website");
            ex.printStackTrace();
            if (callback != null) plugin.sync(() -> callback.accept(-1L));
            return;
        }

        final List<Head> customHeads = new ArrayList<>();
        plugin.logDebug("Loading categories...");
        for (final var entry : readContent.entrySet()) {
            final var id = CategoryID.getCategoryID(entry.getKey());

            if (id.equals(CategoryID.OTHER)) {
                customHeads.addAll(entry.getValue());
                continue;
            }
            categories.put(id, new Category(id, entry.getValue().stream().sorted(Comparator.comparingInt(Head::getId)).toList()));
            categoriesLoaded++;
        }

        final var recentHeads = new ArrayList<Head>();
        for (final var category : categories.values()) {
            recentHeads.addAll(category.getHeads().stream().filter(Head::isRecent).toList());
        }
        categories.put(CategoryID.RECENT, new Category(CategoryID.RECENT, recentHeads.stream().sorted(Comparator.comparingInt(Head::getId)).toList()));

        categories.put(CategoryID.OTHER, new Category(CategoryID.OTHER, customHeads.stream().sorted(Comparator.comparingInt(Head::getId)).toList()));

        databaseLoaded = true;

        final long loadTime = System.currentTimeMillis() - startTime;
        EventManager.callDatabaseLoadEvent(loadTime);
        if (callback != null) plugin.sync(() -> callback.accept(loadTime));
        plugin.getLogger().info("Successfully loaded " + headsLoaded + " heads with " + categoriesLoaded + " categories in %sms!".formatted(loadTime));

        plugin.sync(plugin.getGuiManager()::loadTemplates);
    }

    /*public void showSearchChatPrompt(Player player, Category category, boolean namesOnly) {
        ChatPrompt.showPrompt(plugin, player, plugin.getLocale().getMessage(player, "general.search.input").asString(), (event ->
                doSearch(player, category, event.getMessage(), namesOnly))).setOnCancel(() -> plugin.getLocale().getMessage(player, "general.search.cancelled").send(player)
        );
    }*/

    public void doSearch(Player player, Category category, String params, boolean namesOnly) {
        if (Settings.main().searchingMessage) {
            player.sendRichMessage(plugin.getLocale().getMessage("general", "search", "searching")
                    .placeholders(Pair.of("args", params))
                    .asString());
        }
        final var searchCategories = category != null ? List.of(category) : categories.values();
        plugin.logDebug("Search heads in " + searchCategories.size() + " categories");

        final var foundHeads = new ArrayList<Head>();
        for (Category c : searchCategories) {
            foundHeads.addAll(c.getHeads().stream()
                    .filter(head -> head.getName().toLowerCase().contains(params.toLowerCase()))
                    .toList());
            if (!namesOnly) {
                foundHeads.addAll(c.getHeads().stream()
                        .filter(head -> head.getTags().contains(params.toLowerCase()))
                        .toList());
            }
        }
        plugin.logDebug("Found " + foundHeads.size() + " heads");

        final var event = new PlayerSearchHeadsEvent(player, params, new Category(CategoryID.SEARCH, foundHeads.stream().sorted(Comparator.comparingInt(Head::getId)).toList()));
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return;

        plugin.getGuiManager().openHeads(player, (Category) event.getResults());
    }

    @Override
    public List<me.denarydev.heads.api.head.Category> getAllCategories() {
        return new ArrayList<>(categories.values());
    }

    @Override
    public Category getCategory(CategoryID id) {
        return categories.values().stream().filter(category -> category.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public ItemStack getHead(int id) {
        final var category = categories.values().stream().filter(c -> c.getHead(id) != null).findFirst().orElse(null);
        if (category != null) {
            return ItemUtils.getCustomHead(category.getHead(id).getBase64());
        }
        return null;
    }

    @Override
    public Category getRecentHeads() {
        return categories.get(CategoryID.RECENT);
    }
}
