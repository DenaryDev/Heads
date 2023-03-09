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
import me.denarydev.heads.api.head.CategoryID;
import me.denarydev.heads.api.head.HeadsManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
        plugin.logDebug("Loading database...");
        databaseLoaded = false;
        categories.clear();
        headsLoaded = 0;
        categoriesLoaded = 0;

        Map<String, List<Head>> readContent = new TreeMap<>();
        try {
            plugin.logDebug("Fetching heads from minecraft-heads.com website");
            String headDBUrl = "https://minecraft-heads.4lima.de/csv/2022-02-25-ZgFDreHnLiGvHdf3RFfgg/Custom-Head-DB.csv";
            URL url = new URL(headDBUrl);
            URLConnection connection = url.openConnection();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] values = line.split(";");
                    String category = values[0];
                    Head head = new Head(CategoryID.getCategoryID(values[0]), Integer.parseInt(values[1]), values[2].replace("\"", ""), values[3], Integer.valueOf(values[4]).equals(1), values.length == 5 ? Collections.emptyList() : List.of(values[5].replace("\"", "").toLowerCase().split("\\|")));

                    List<Head> heads = new ArrayList<>();
                    if (!readContent.containsKey(category)) {
                        heads.add(head);
                    } else {
                        heads = new ArrayList<>(readContent.get(category));
                        heads.add(head);
                        readContent.remove(category);
                    }
                    readContent.put(category, heads);
                    headsLoaded++;
                }
            }
        } catch (IOException ex) {
            plugin.logError("Error fetching heads from website");
            ex.printStackTrace();
            return;
        }

        List<Head> customHeads = new ArrayList<>();
        plugin.logDebug("Loading categories...");
        for (Map.Entry<String, List<Head>> entry : readContent.entrySet()) {
            CategoryID id = CategoryID.getCategoryID(entry.getKey());

            if (id.equals(CategoryID.OTHER)) {
                customHeads = entry.getValue();
                continue;
            }
            categories.put(id, new Category(id, entry.getValue().stream().sorted(Comparator.comparingInt(Head::getId)).toList()));
            categoriesLoaded++;
        }

        final var recentHeads = new ArrayList<Head>();
        for (Category category : categories.values()) {
            recentHeads.addAll(category.getHeads().stream().filter(Head::isRecent).toList());
        }
        categories.put(CategoryID.RECENT, new Category(CategoryID.RECENT, recentHeads.stream().sorted(Comparator.comparingInt(Head::getId)).toList()));

        categories.put(CategoryID.OTHER, new Category(CategoryID.OTHER, customHeads.stream().sorted(Comparator.comparingInt(Head::getId)).toList()));

        databaseLoaded = true;
        HeadsPlugin.getInstance().log("Successfully loaded " + headsLoaded + " heads in " + categoriesLoaded + " categories!");
        plugin.runSync(plugin.getGuiManager()::loadTemplates);
    }

    public void showSearchChatPrompt(Player player, Category category, boolean namesOnly) {
        //ChatPrompt.showPrompt(plugin, player, plugin.getLocale().getMessage(player, "general.search.input").asString(), (event ->
        //        doSearch(player, category, event.getMessage(), namesOnly))).setOnCancel(() -> plugin.getLocale().getMessage(player, "general.search.cancelled").send(player)
        //);
    }

    public void doSearch(Player player, Category category, String args, boolean namesOnly) {
        player.sendRichMessage(plugin.getLocale().getMessage("general", "search", "searching")
                .placeholders(
                        Pair.of("args", args)
                ).asString());
        final var searchCategories = category != null ? List.of(category) : categories.values();
        plugin.logDebug("Search in " + searchCategories.size() + " categories");

        final var foundHeads = new ArrayList<Head>();
        for (Category c : searchCategories) {
            foundHeads.addAll(c.getHeads().stream()
                    .filter(head -> head.getName().toLowerCase().contains(args.toLowerCase()))
                    .toList());
            if (!namesOnly) {
                foundHeads.addAll(c.getHeads().stream()
                        .filter(head -> head.getTags().contains(args.toLowerCase()))
                        .toList());
            }
        }
        plugin.logDebug("Found " + foundHeads.size() + " heads");

        plugin.getGuiManager().openHeads(player, new Category(CategoryID.SEARCH, foundHeads.stream().sorted(Comparator.comparingInt(Head::getId)).toList()));
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
        Category category = categories.values().stream().filter(c -> c.getHead(id) != null).findFirst().orElse(null);
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
