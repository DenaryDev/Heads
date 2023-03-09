/*
 * Copyright (c) 2023 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package me.arcaniax.hdb.api;

import me.arcaniax.hdb.api.enums.CategoryEnum;
import me.denarydev.heads.HeadsPlugin;
import me.denarydev.heads.api.head.CategoryID;
import me.denarydev.heads.head.Head;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

@Deprecated
public class HeadDatabaseAPI {

    /*======================== Implemented ========================*/

    public boolean isHead(String ID) {
        return getHead(ID) != null;
    }

    public String getBase64(String id) {
        final var head = getHead(id);
        if (head != null) return head.getBase64();
        return null;
    }

    public ItemStack getItemHead(String id) {
        final var head = getHead(id);
        if (head != null) return head.getItem();
        return null;
    }

    public List<Head> getHeads(CategoryEnum category) {
        return HeadsPlugin.getInstance().getHeadDBManager().getCategory(CategoryID.valueOf(category.name())).getHeads();
    }

    public ItemStack getRandomHead() {
        return getItemHead("1");
    }

    public CategoryEnum getCategory(String id) {
        try {
            return CategoryEnum.valueOf(id);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    /*======================== Not Implemented ========================*/

    public void setPrefixID(String prefix) {
    }

    public boolean isDecorativeHead(Block block) {
        return false;
    }

    public boolean isDecorativeHead(ItemStack itemStack) {
        return false;
    }

    public ItemStack getItemHead(Block block) {
        return null;
    }

    public String getItemID(ItemStack itemStack) {
        return null;
    }

    public String getBlockID(Block block) {
        return null;
    }

    public String getBase64(ItemStack itemStack) {
        return null;
    }

    public String getBase64(Block block) {
        return null;
    }

    public boolean addHead(CategoryEnum category, Head head) {
        return false;
    }

    public boolean setBlockSkin(Block block, String id) {
        return false;
    }

    public String addHead(CategoryEnum category, String name, UUID uuid) {
        return null;
    }

    public String addHead(CategoryEnum category, String name, String base64) {
        return null;
    }

    public boolean removeHead(String id) {
        return false;
    }

    /*======================== Internal ========================*/

    private Head getHead(String id) {
        final var categories = HeadsPlugin.getInstance().getHeadDBManager().getCategories().values();
        for (final var category : categories) {
            for (final var head : category.getHeads()) {
                if (head.getId() == Integer.parseInt(id)) {
                    return head;
                }
            }
        }
        return null;
    }
}
