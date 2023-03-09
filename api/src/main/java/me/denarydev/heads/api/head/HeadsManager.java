/*
 * Copyright (c) 2022 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package me.denarydev.heads.api.head;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface HeadsManager {

    List<Category> getAllCategories();

    Category getCategory(CategoryID id);

    boolean isDatabaseLoaded();

    int getHeadsLoaded();

    int getCategoriesLoaded();

    default ItemStack getHead(String id) {
        return getHead(Integer.parseInt(id));
    }

    ItemStack getHead(int id);

    Category getRecentHeads();
}
