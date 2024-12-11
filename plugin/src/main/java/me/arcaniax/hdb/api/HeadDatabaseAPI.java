/*
 * Head Database is a fast and user-friendly plugin to allow thousands of custom heads for Minecraft.
 * Copyright (C) Arcaniax-Development
 * Copyright (C) Arcaniax team and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package me.arcaniax.hdb.api;

import me.arcaniax.hdb.enums.CategoryEnum;
import me.arcaniax.hdb.object.head.Head;
import me.denarydev.heads.HeadsPlugin;
import me.denarydev.heads.api.head.CategoryID;
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
        if (head != null) return head.getHandle().base64();
        return null;
    }

    public ItemStack getItemHead(String id) {
        final var head = getHead(id);
        if (head != null) return head.getHandle().itemStack();
        return null;
    }

    public List<Head> getHeads(CategoryEnum category) {
        return HeadsPlugin.getInstance().getHeadDBManager().getCategory(CategoryID.valueOf(category.name())).getHeads()
                .stream().map(Head::new).toList();
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
                if (head.id() == Integer.parseInt(id)) {
                    return new Head(head);
                }
            }
        }
        return null;
    }
}
