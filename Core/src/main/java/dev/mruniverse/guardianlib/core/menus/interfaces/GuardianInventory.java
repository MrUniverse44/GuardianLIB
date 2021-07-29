package dev.mruniverse.guardianlib.core.menus.interfaces;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;

public interface GuardianInventory {
    String getInventoryID();

    GuardianInventory setID(String menuID);

    GuardianInventory setClickCancellable(boolean cancellable);

    GuardianInventory setItems(FileConfiguration fileConfiguration, GuardianItems[] itemIdentifier);

    GuardianInventory setItems(HashMap<ItemStack,GuardianItems> items, HashMap<ItemStack,Integer> itemSlot);

    HashMap<ItemStack,GuardianItems> getItems();

    HashMap<ItemStack,Integer> getItemsSlot();

    GuardianItems[] getIdentifier();

    boolean isCancellable();

    void register(Plugin plugin);

    void giveInventory(Player player,boolean clearInventory);

    void updateItems(FileConfiguration fileConfiguration);

    void updateItems(FileConfiguration fileConfiguration,GuardianItems[] itemIdentifier);

    void updateItems(HashMap<ItemStack,GuardianItems> items);

    void updateItems(HashMap<ItemStack,GuardianItems> items, HashMap<ItemStack,Integer> itemSlot);
}

