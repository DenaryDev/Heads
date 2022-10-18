/*
 * Copyright (c) 2022 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package io.sapphiremc.heads.hdb;

import com.songoda.core.utils.ItemUtils;
import io.sapphiremc.heads.HeadsPlugin;
import io.sapphiremc.heads.api.head.CategoryID;
import io.sapphiremc.heads.utils.Formatter;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Category implements io.sapphiremc.heads.api.head.Category {

    @Getter
    private final CategoryID id;
    @Getter
    private final List<Head> heads;
    @Getter
    private final ItemStack menuItem;

    public Category(CategoryID id, List<Head> heads) {
        this.id = id;
        this.heads = heads;
        this.menuItem = ItemUtils.getCustomHead(HeadsPlugin.getInstance().getConfiguration().getString("category." + this.id.toLower() + ".texture"));
    }

    @Override
    public List<io.sapphiremc.heads.api.head.Head> getAllHeads() {
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
        return Formatter.formatCount(getHeadsCount());
    }

    @Override
    public String getName() {
        return getName(null);
    }

    @Override
    public String getName(Player player) {
        return HeadsPlugin.getInstance().getLanguageManager().getMessage(player, "category." + id.toLower()).asString();
    }
}
