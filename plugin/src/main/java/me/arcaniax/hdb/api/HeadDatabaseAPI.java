/*
 * Copyright (c) 2023 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package me.arcaniax.hdb.api;

import me.denarydev.crystal.utils.ItemUtils;
import me.denarydev.heads.HeadsPlugin;
import org.bukkit.inventory.ItemStack;

@Deprecated
public class HeadDatabaseAPI {

    @Deprecated
    public ItemStack getItemHead(String id) {
        final var plugin = HeadsPlugin.getInstance();
        final var categories = plugin.getHeadDBManager().getCategories().values();
        for (final var category : categories) {
            for (final var head : category.getHeads()) {
                if (head.getId() == Integer.parseInt(id)) {
                    return ItemUtils.getCustomHead(head.getBase64());
                }
            }
        }
        return null;
    }
}
