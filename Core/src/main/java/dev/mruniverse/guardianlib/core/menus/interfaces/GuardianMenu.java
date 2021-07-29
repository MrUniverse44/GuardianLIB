package dev.mruniverse.guardianlib.core.menus.interfaces;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public interface GuardianMenu {
    Menus getMenu();

    GuardianMenu setInventoryOwner(@Nullable InventoryHolder owner);

    GuardianMenu setMenu(Menus menuName);

    GuardianMenu setClickCancellable(boolean cancellable);

    GuardianMenu setRows(int size);

    GuardianMenu setTitle(String menuTitle);

    GuardianMenu setItems(FileConfiguration fileConfiguration, GuardianItems[] itemIdentifier);

    GuardianMenu setItems(HashMap<ItemStack,GuardianItems> items, HashMap<ItemStack,Integer> itemSlot);

    HashMap<ItemStack,GuardianItems> getItems();

    HashMap<ItemStack,Integer> getItemsSlot();

    GuardianMenu createMenu();

    GuardianItems[] getIdentifier();

    Inventory getInventory();

    boolean isCancellable();

    void pasteInventoryItems();

    void register(Plugin plugin);

    void updateItems();

    void showMenu(Player player);

    void updateItems(FileConfiguration fileConfiguration);

    void updateItems(FileConfiguration fileConfiguration,GuardianItems[] itemIdentifier);

    void updateItems(HashMap<ItemStack,GuardianItems> items);

    void updateItems(HashMap<ItemStack,GuardianItems> items, HashMap<ItemStack,Integer> itemSlot);
}
