package dev.mruniverse.guardianlib.core.events;

import dev.mruniverse.guardianlib.core.enums.InteractType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
@SuppressWarnings("unused")
public class HologramInteractEvent extends Event {
    private final String holoID;
    private final int holoLineID;
    private final Player player;
    private final InteractType interactType;
    private static final HandlerList handlerList = new HandlerList();
    public HologramInteractEvent(String holoID, int holoLineID, Player player, InteractType interactType) {
        this.holoID = holoID;
        this.holoLineID = holoLineID;
        this.player = player;
        this.interactType = interactType;
    }
    public String getHoloID() {
        return holoID;
    }
    public int getHoloLineID() {
        return holoLineID;
    }
    public Player getPlayer() {
        return player;
    }
    public InteractType getInteractType() {
        return interactType;
    }
    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }
}
