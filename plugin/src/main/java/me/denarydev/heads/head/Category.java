/*
 * Copyright (c) 2022 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package me.denarydev.heads.head;

import lombok.Getter;
import me.denarydev.crystal.utils.ItemUtils;
import me.denarydev.crystal.utils.NumberUtils;
import me.denarydev.heads.HeadsPlugin;
import me.denarydev.heads.Settings;
import me.denarydev.heads.api.head.CategoryID;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Category implements me.denarydev.heads.api.head.Category {

    @Getter
    private final CategoryID id;
    @Getter
    private final List<Head> heads;
    @Getter
    private final ItemStack menuItem;

    public Category(CategoryID id, List<Head> heads) {
        this.id = id;
        this.heads = heads;
        final var categories = Settings.main().categories;
        this.menuItem = ItemUtils.getCustomHead(switch (this.id) {
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
            case RECENT, FAVORITES -> getRandomHead().getBase64();
            case SEARCH -> null;
        });
    }

    @Override
    public List<me.denarydev.heads.api.head.Head> getAllHeads() {
        return new ArrayList<>(heads);
    }

    @Override
    public Head getHead(int id) {
        return heads.stream().filter(head -> head.getId() == id).findFirst().orElse(null);
    }

    @Override
    public int getHeadsCount() {
        return heads.size();
    }

    @Override
    public String getHeadsCountFormatted() {
        return NumberUtils.formatWithSuffix(getHeadsCount());
    }

    @Override
    public String getName() {
        return HeadsPlugin.getInstance().getLocale().getMessage("category." + id.toLower()).asString();
    }

    @Override
    public me.denarydev.heads.api.head.Head getRandomHead() {
        final var random = new Random();
        return heads.get(random.nextInt(0, heads.size() - 1));
    }
}
