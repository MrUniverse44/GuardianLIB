package dev.mruniverse.guardianlib.core.menus.inventory;

import dev.mruniverse.guardianlib.core.events.GuardianInventoryClickEvent;
import dev.mruniverse.guardianlib.core.menus.interfaces.GuardianInventory;
import dev.mruniverse.guardianlib.core.menus.interfaces.GuardianItems;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class InventoryListener implements Listener {
    private final GuardianInventory guardianInventory;

    public InventoryListener(GuardianInventory guardianInventory) {
        this.guardianInventory = guardianInventory;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getItem() != null) {
            if (event.getItem() == null) return;
            if (event.getItem().getItemMeta() == null) return;

            for(Map.Entry<ItemStack, GuardianItems> entry : guardianInventory.getItems().entrySet()) {
                ItemStack item = entry.getKey();
                if(event.getItem().getType().equals(item.getType()) && event.getItem().getItemMeta().equals(item.getItemMeta())) {
                    event.setCancelled(guardianInventory.isCancellable());
                    GuardianInventoryClickEvent customEvent = new GuardianInventoryClickEvent(event.getPlayer(),entry.getValue(),guardianInventory.getInventoryID());
                    Bukkit.getServer().getPluginManager().callEvent(customEvent);
                }
            }
            /*
             *
             */

        }
    }

    @EventHandler
    public void onInventory(InventoryClickEvent event) {
        ItemStack currentItem = event.getCurrentItem();

        if (currentItem != null) {
            if (currentItem.getItemMeta() == null) return;

            for(Map.Entry<ItemStack, GuardianItems> entry : guardianInventory.getItems().entrySet()) {
                ItemStack item = entry.getKey();
                if(currentItem.getType().equals(item.getType()) && currentItem.getItemMeta().equals(item.getItemMeta())) {
                    event.setCancelled(guardianInventory.isCancellable());
                    Player player = (Player)event.getWhoClicked();
                    GuardianInventoryClickEvent customEvent = new GuardianInventoryClickEvent(player,entry.getValue(),guardianInventory.getInventoryID());
                    Bukkit.getServer().getPluginManager().callEvent(customEvent);
                }
            }
        }
    }
}