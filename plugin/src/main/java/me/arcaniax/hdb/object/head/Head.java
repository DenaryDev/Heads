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

package me.arcaniax.hdb.object.head;

import lombok.Getter;
import me.arcaniax.hdb.enums.CategoryEnum;
import me.denarydev.heads.api.head.CategoryID;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.UUID;

@Deprecated
public class Head {

    public String b64;
    public String name;
    public String lore;
    public String id;
    public String uuid;
    public CategoryEnum c;
    public boolean recent;
    public Collection<String> tags;
    public boolean isCustom;
    long ID;
    long number;
    private double price = -1.0D;

    public double getPrice() {
        return this.price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public boolean search(String input) {
        return false;
    }

    public ItemStack getHead() {
        return handle.itemStack();
    }

    /*======================== Internal ========================*/
    @Getter
    private final me.denarydev.heads.head.Head handle;

    public Head(me.denarydev.heads.head.Head head) {
        this.handle = head;
        this.b64 = head.base64();
        this.name = head.name();
        this.lore = head.tagsString();
        this.id = head.name().toLowerCase();
        this.uuid = UUID.randomUUID().toString();
        this.c = CategoryEnum.fromId(head.categoryID());
        this.recent = head.recent();
        this.tags = head.tags();
        this.isCustom = head.categoryID() == CategoryID.OTHER;
        this.ID = head.id();
        this.number = head.id();
    }
}
