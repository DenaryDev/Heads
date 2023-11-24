package me.arcaniax.hdb.api;

import me.arcaniax.hdb.api.enums.CategoryEnum;
import me.arcaniax.hdb.api.enums.EconomyEnum;
import me.denarydev.heads.head.Head;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@Deprecated
public class PlayerClickHeadEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancel = false;

    private final Head head;

    public PlayerClickHeadEvent(@NotNull final Player player, @NotNull final Head head) {
        super(player);
        this.head = head;
    }

    public CategoryEnum getCategoryEnum() {
        return CategoryEnum.fromId(head.categoryID());
    }

    public double getPrice() {
        return 0.0D;
    }

    public String getHeadID() {
        return String.valueOf(head.id());
    }

    public boolean isEconomy() {
        return false;
    }

    public EconomyEnum getEconomyEnum() {
        return EconomyEnum.CURRENCY;
    }

    public ItemStack getHead() {
        return head.itemStack();
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
