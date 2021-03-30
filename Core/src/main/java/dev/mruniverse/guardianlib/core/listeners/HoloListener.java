package dev.mruniverse.guardianlib.core.listeners;

import dev.mruniverse.guardianlib.core.GuardianLIB;
import dev.mruniverse.guardianlib.core.enums.InteractType;
import dev.mruniverse.guardianlib.core.events.HologramInteractEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class HoloListener implements Listener {
    private final GuardianLIB plugin;
    public HoloListener(GuardianLIB guardianLIB) {
        plugin = guardianLIB;
    }
    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent e){
        Entity entity = e.getRightClicked();
        EntityType entityType = entity.getType();
        Player player = e.getPlayer();
        if(entityType == EntityType.ARMOR_STAND) {
            for(Map.Entry<Integer,List<ArmorStand>> entry : plugin.getArmorStandsUsingPrivateID().entrySet()) {
                if(checkArmor(entity.getUniqueId(),entry.getValue())) {
                    Bukkit.getPluginManager().callEvent(new HologramInteractEvent("SH-" + entry.getKey(),0,player, InteractType.INTERACT_AT));
                    e.setCancelled(true);
                    return;
                }
            }
        }
    }

    private boolean checkArmor(UUID uuid, List<ArmorStand> armorList) {
        for(ArmorStand armor : armorList) { if(uuid == armor.getUniqueId()) return true; }
        return false;
    }
}
