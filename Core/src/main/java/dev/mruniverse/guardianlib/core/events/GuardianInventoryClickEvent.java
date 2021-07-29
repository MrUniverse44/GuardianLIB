package dev.mruniverse.guardianlib.core.events;

import dev.mruniverse.guardianlib.core.menus.interfaces.GuardianItems;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class GuardianInventoryClickEvent extends Event {

    private static final HandlerList handlerList = new HandlerList();

    private final Player player;

    private final GuardianItems currentIdentifier;

    private final String inventoryID;

    public GuardianInventoryClickEvent(Player player, GuardianItems currentIdentifier,String inventoryID) {
        this.player = player;
        this.currentIdentifier = currentIdentifier;
        this.inventoryID = inventoryID;
    }

    public Player getPlayer() {
        return player;
    }

    public GuardianItems getIdentifier() {
        return currentIdentifier;
    }

    public String getInventoryID() {
        return inventoryID;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }
}
