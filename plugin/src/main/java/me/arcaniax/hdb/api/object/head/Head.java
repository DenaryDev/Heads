package me.arcaniax.hdb.api.object.head;

import lombok.Getter;
import me.arcaniax.hdb.api.enums.CategoryEnum;
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
