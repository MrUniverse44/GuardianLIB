package dev.mruniverse.guardianlib.core.menus.gui;

import dev.mruniverse.guardianlib.core.events.GuardianMenuClickEvent;
import dev.mruniverse.guardianlib.core.menus.interfaces.GuardianItems;
import dev.mruniverse.guardianlib.core.menus.interfaces.GuardianMenu;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class MenuListener implements Listener {
    private final GuardianMenu guardianMenu;

    public MenuListener(GuardianMenu guardianMenu) {
        this.guardianMenu = guardianMenu;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getItem() != null) {
            if (event.getItem() == null) return;
            if (event.getItem().getItemMeta() == null) return;

            for(Map.Entry<ItemStack, GuardianItems> entry : guardianMenu.getItems().entrySet()) {
                ItemStack item = entry.getKey();
                if(event.getItem().getType().equals(item.getType()) && event.getItem().getItemMeta().equals(item.getItemMeta())) {
                    event.setCancelled(guardianMenu.isCancellable());
                    GuardianMenuClickEvent customEvent = new GuardianMenuClickEvent(event.getPlayer(),entry.getValue(),guardianMenu.getMenu());
                    Bukkit.getServer().getPluginManager().callEvent(customEvent);
                }
            }
            /*
             *
             */

        }
    }
}
