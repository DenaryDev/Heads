/*
 * Copyright (c) 2022 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package io.sapphiremc.heads.hdb;

import com.songoda.core.input.ChatPrompt;
import com.songoda.core.utils.ItemUtils;
import io.sapphiremc.heads.HeadsPlugin;
import io.sapphiremc.heads.api.head.CategoryID;
import io.sapphiremc.heads.api.head.HeadsManager;
import io.sapphiremc.heads.gui.GuiHeads;
import lombok.Getter;
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
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class HeadDBManager implements HeadsManager {

    private final HeadsPlugin plugin;
    @Getter
    private final List<Category> categories = new ArrayList<>();

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

            if (id.equals(CategoryID.CUSTOM)) {
                customHeads = entry.getValue();
                continue;
            }
            categories.add(new Category(id, entry.getValue().stream().sorted(Comparator.comparingInt(Head::getId)).toList()));
            categoriesLoaded++;
        }

        categories.add(new Category(CategoryID.CUSTOM, customHeads.stream().toList()));
        databaseLoaded = true;
        HeadsPlugin.getInstance().log("Successfully loaded " + headsLoaded + " heads in " + categoriesLoaded + " categories!");
    }

    public void showSearchChatPrompt(Player player, Category category, boolean namesOnly) {
        ChatPrompt.showPrompt(plugin, player, plugin.getLanguageManager().getMessage(player, "general.search.input").asString(), (event ->
                doSearch(player, category, event.getMessage(), namesOnly))).setOnCancel(() -> plugin.getLanguageManager().getMessage(player, "general.search.cancelled").send(player)
        );
    }

    public void doSearch(Player player, Category category, String args, boolean namesOnly) {
        player.sendMessage(plugin.getLanguageManager().getMessage("general.search.searching").processPlaceholder("%args%", args).asString());
        List<Head> searchHeads = new ArrayList<>();
        if (category != null) {
            plugin.logDebug("Search in category " + category.getName());
            searchHeads.addAll(category.getHeads().stream()
                    .filter(head -> head.getName().toLowerCase().contains(args.toLowerCase()))
                    .toList());
            if (!namesOnly) {
                searchHeads.addAll(category.getHeads().stream()
                        .filter(head -> head.getTags().contains(args.toLowerCase()))
                        .toList());
            }
        } else {
            plugin.logDebug("Search in all categories");
            for (Category c : categories) {
                searchHeads.addAll(c.getHeads().stream()
                        .filter(head -> head.getName().toLowerCase().contains(args.toLowerCase()))
                        .toList());
                if (!namesOnly) {
                    searchHeads.addAll(c.getHeads().stream()
                            .filter(head -> head.getTags().contains(args.toLowerCase()))
                            .toList());
                }
            }
        }
        plugin.logDebug("Found " + searchHeads.size() + " heads");
        plugin.showGUI(player, new GuiHeads(player, GuiHeads.Type.SEARCH, null, searchHeads.stream().sorted(Comparator.comparingInt(Head::getId)).toList()));
    }

    @Override
    public List<io.sapphiremc.heads.api.head.Category> getAllCategories() {
        return new ArrayList<>(categories);
    }

    @Override
    public Category getCategory(CategoryID id) {
        return categories.stream().filter(category -> category.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public ItemStack getHead(int id) {
        Category category = categories.stream().filter(c -> c.getHead(id) != null).findFirst().orElse(null);
        if (category != null) {
            return ItemUtils.getCustomHead(category.getHead(id).getBase64());
        }
        return null;
    }

    @Override
    public List<io.sapphiremc.heads.api.head.Head> getRecentHeads() {
        return new ArrayList<>(getRecentlyHeads());
    }

    public List<Head> getRecentlyHeads() {
        List<Head> recentHeads = new ArrayList<>();
        for (Category category : categories) {
            recentHeads.addAll(category.getHeads().stream().filter(Head::isRecent).toList());
        }

        return recentHeads.stream().sorted(Comparator.comparingInt(Head::getId)).toList();
    }
}
