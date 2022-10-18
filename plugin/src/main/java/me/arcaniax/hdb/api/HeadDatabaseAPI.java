/*
 * Copyright (c) 2022 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package me.arcaniax.hdb.api;

import com.songoda.core.utils.ItemUtils;
import io.sapphiremc.heads.HeadsPlugin;
import io.sapphiremc.heads.hdb.Category;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Deprecated
public class HeadDatabaseAPI {

    @Deprecated
    public ItemStack getItemHead(String id) {
        HeadsPlugin plugin = HeadsPlugin.getInstance();
        List<Category> categories = plugin.getHeadDBManager().getCategories();
        for (Category category : categories) {
            for (io.sapphiremc.heads.hdb.Head head : category.getHeads()) {
                if (head.getId() == Integer.parseInt(id)) {
                    return ItemUtils.getCustomHead(head.getBase64());
                }
            }
        }
        return null;
    }
}
