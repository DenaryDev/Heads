package me.arcaniax.hdb.api.object.head;

import me.arcaniax.hdb.api.enums.CategoryEnum;
import org.apache.logging.log4j.util.Strings;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;

@Deprecated
public class Head {

    public String b64 = Strings.EMPTY;
    public String name = Strings.EMPTY;
    public String lore = Strings.EMPTY;
    public String id = Strings.EMPTY;
    public String uuid = Strings.EMPTY;
    public CategoryEnum c = CategoryEnum.DISABLED;
    public boolean recent = false;
    public Collection<String> tags = new ArrayList<>();
    public boolean isCustom = false;
    long ID = 0L;
    long number = 0L;
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
        return null;
    }
}
