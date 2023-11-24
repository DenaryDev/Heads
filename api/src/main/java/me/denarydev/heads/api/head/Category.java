/*
 * Copyright (c) 2023 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package me.denarydev.heads.api.head;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Category class
 */
public interface Category {

    CategoryID getId();

    List<Head> getAllHeads();

    default Head getHead(String id) {
        return getHead(Integer.parseInt(id));
    }

    Head getHead(int id);

    ItemStack getMenuItem();

    int getHeadsCount();

    String getHeadsCountFormatted();

    String getName();

    Head getRandomHead();
}
