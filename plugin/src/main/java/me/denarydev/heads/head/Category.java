/*
 * Copyright (c) 2024 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package me.denarydev.heads.head;

import lombok.Getter;
import me.denarydev.crystal.utils.ItemUtils;
import me.denarydev.crystal.utils.NumberUtils;
import me.denarydev.heads.Settings;
import me.denarydev.heads.api.head.CategoryID;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Getter
public class Category implements me.denarydev.heads.api.head.Category {

    private final CategoryID id;
    private final List<Head> heads;
    private final ItemStack menuItem;

    public Category(CategoryID id, List<Head> heads) {
        this.id = id;
        this.heads = heads;
        final var categories = Settings.main().categories;
        this.menuItem = ItemUtils.createHead(switch (this.id) {
            case ALPHABET -> categories.alphabet;
            case ANIMALS -> categories.animals;
            case BLOCKS -> categories.blocks;
            case DECORATION -> categories.decoration;
            case FOOD_DRINKS -> categories.food_drinks;
            case HUMANS -> categories.humans;
            case HUMANOID -> categories.humanoid;
            case MISCELLANEOUS -> categories.miscellaneous;
            case MONSTERS -> categories.monsters;
            case PLANTS -> categories.plants;
            case OTHER -> categories.custom;
            case RECENT, FAVORITES -> getRandomHead().base64();
            case SEARCH -> null;
        });
    }

    @Override
    public List<me.denarydev.heads.api.head.Head> getAllHeads() {
        return new ArrayList<>(heads);
    }

    @Override
    public Head getHead(int id) {
        return heads.stream().filter(head -> head.id() == id).findFirst().orElse(null);
    }

    @Override
    public int getHeadsCount() {
        return heads.size();
    }

    @Override
    public String getHeadsCountFormatted() {
        return NumberUtils.formatNumber(getHeadsCount());
    }

    @Override
    public String getName() {
        return switch (this.id) {
            case ALPHABET -> Settings.messages().categories.alphabet.name;
            case ANIMALS -> Settings.messages().categories.animals.name;
            case BLOCKS -> Settings.messages().categories.blocks.name;
            case DECORATION -> Settings.messages().categories.decoration.name;
            case FOOD_DRINKS -> Settings.messages().categories.food_drinks.name;
            case HUMANS -> Settings.messages().categories.humans.name;
            case HUMANOID -> Settings.messages().categories.humanoid.name;
            case MISCELLANEOUS -> Settings.messages().categories.miscellaneous.name;
            case MONSTERS -> Settings.messages().categories.monsters.name;
            case PLANTS -> Settings.messages().categories.plants.name;
            case RECENT -> Settings.messages().categories.recent.name;
            case FAVORITES -> Settings.messages().categories.favorites.name;
            case SEARCH -> Settings.messages().categories.search.name;
            case OTHER -> Settings.messages().categories.other.name;
        };
    }

    @Override
    public List<String> getDescription() {
        return switch (this.id) {
            case ALPHABET -> Settings.messages().categories.alphabet.description;
            case ANIMALS -> Settings.messages().categories.animals.description;
            case BLOCKS -> Settings.messages().categories.blocks.description;
            case DECORATION -> Settings.messages().categories.decoration.description;
            case FOOD_DRINKS -> Settings.messages().categories.food_drinks.description;
            case HUMANS -> Settings.messages().categories.humans.description;
            case HUMANOID -> Settings.messages().categories.humanoid.description;
            case MISCELLANEOUS -> Settings.messages().categories.miscellaneous.description;
            case MONSTERS -> Settings.messages().categories.monsters.description;
            case PLANTS -> Settings.messages().categories.plants.description;
            case RECENT -> Settings.messages().categories.recent.description;
            case FAVORITES -> Settings.messages().categories.favorites.description;
            case SEARCH -> Settings.messages().categories.search.description;
            case OTHER -> Settings.messages().categories.other.description;
        };
    }

    @Override
    public me.denarydev.heads.api.head.Head getRandomHead() {
        final var random = ThreadLocalRandom.current();
        return heads.get(random.nextInt(0, heads.size() - 1));
    }
}
