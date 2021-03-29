package dev.mruniverse.guardianlib.core.listeners;

import dev.mruniverse.guardianlib.core.GuardianLIB;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {
    private final GuardianLIB plugin;
    public JoinListener(GuardianLIB plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        plugin.getNMS().injectPlayer(player);
    }
}
