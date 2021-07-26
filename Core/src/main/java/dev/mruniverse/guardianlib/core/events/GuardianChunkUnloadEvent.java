package dev.mruniverse.guardianlib.core.events;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class GuardianChunkUnloadEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private boolean cancel = false;

    private final Chunk chunk;

    private final World world;

    public GuardianChunkUnloadEvent(Chunk chunk, World world) {
        this.chunk = chunk;
        this.world = world;
    }


    public boolean isCancelled() {
        return this.cancel;
    }

    public Chunk getChunk() {
        return chunk;
    }

    public World getWorld() {
        return world;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
