package dev.mruniverse.guardianlib.core.menus.inventory;

import dev.mruniverse.guardianlib.core.GuardianLIB;
import dev.mruniverse.guardianlib.core.menus.interfaces.GuardianInventory;
import dev.mruniverse.guardianlib.core.menus.interfaces.GuardianItems;
import dev.mruniverse.guardianlib.core.utils.xseries.XMaterial;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GuardianInventoryBuilder implements GuardianInventory {
    private String currentID;

    private GuardianItems[] currentIdentifier;

    private HashMap<ItemStack,Integer> MENUS_ITEM_SLOT;

    private HashMap<ItemStack,GuardianItems> MENUS_ITEMS;

    private FileConfiguration fileConfiguration = null;

    private boolean cancellable = true;

    @Override
    public GuardianInventory setID(String menuID) {
        this.currentID = menuID;
        return this;
    }

    @Override
    public GuardianInventory setClickCancellable(boolean cancellable) {
        this.cancellable = cancellable;
        return this;
    }

    @Override
    public void giveInventory(Player player,boolean clearInventory) {
        if(clearInventory) {
            player.getInventory().clear();
        }
        pasteInventoryItems(player);
    }

    @Override
    public String getInventoryID() {
        return currentID;
    }

    public void pasteInventoryItems(Player player) {
        for(Map.Entry<ItemStack,Integer> data : MENUS_ITEM_SLOT.entrySet()) {
            player.getInventory().setItem(data.getValue(),data.getKey());
        }
        player.updateInventory();
    }

    @Override
    public GuardianInventory setItems(FileConfiguration fileConfiguration, GuardianItems[] itemIdentifier) {
        this.fileConfiguration = fileConfiguration;
        this.currentIdentifier = itemIdentifier;
        return this;
    }

    @Override
    public GuardianInventory setItems(HashMap<ItemStack,GuardianItems> items, HashMap<ItemStack, Integer> itemSlot) {
        this.MENUS_ITEMS = items;
        this.MENUS_ITEM_SLOT = itemSlot;
        return this;
    }

    @Override
    public HashMap<ItemStack,GuardianItems> getItems() {
        return MENUS_ITEMS;
    }

    @Override
    public HashMap<ItemStack,Integer> getItemsSlot() {
        return MENUS_ITEM_SLOT;
    }

    @Override
    public GuardianItems[] getIdentifier() {
        return currentIdentifier;
    }

    @Override
    public boolean isCancellable() {
        return cancellable;
    }

    @Override
    public void register(Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(new InventoryListener(this),plugin);
    }

    @Override
    public void updateItems(FileConfiguration fileConfiguration) {
        this.fileConfiguration = fileConfiguration;
        updateNow();
    }

    @Override
    public void updateItems(FileConfiguration fileConfiguration,GuardianItems[] itemIdentifier) {
        this.fileConfiguration = fileConfiguration;
        this.currentIdentifier = itemIdentifier;
        updateNow();
    }

    private void updateNow() {
        int slot;
        Optional<XMaterial> optional;
        ItemStack currentItem;
        MENUS_ITEMS.clear();
        MENUS_ITEM_SLOT.clear();
        for(GuardianItems item : currentIdentifier) {
            String path = item.getPath();
            if(fileConfiguration.getBoolean(path + "toggle")) {
                String name = fileConfiguration.getString(path + "name","Unknown Item Name");
                String material = fileConfiguration.getString(path + "item","BEDROCK");
                List<String> lore = fileConfiguration.getStringList(path + "lore");
                slot = fileConfiguration.getInt(path + "slot");
                optional = XMaterial.matchXMaterial(material);
                currentItem = optional.map(xMaterial -> GuardianLIB.getControl().getUtils().getItem(xMaterial, name, lore)).orElse(null);
                MENUS_ITEMS.put(currentItem,item);
                MENUS_ITEM_SLOT.put(currentItem,slot);
            }
        }
    }

    @Override
    public void updateItems(HashMap<ItemStack,GuardianItems> items) {
        MENUS_ITEMS.clear();
        MENUS_ITEMS = items;
    }

    @Override
    public void updateItems(HashMap<ItemStack,GuardianItems> items, HashMap<ItemStack, Integer> itemSlot) {
        MENUS_ITEMS.clear();
        MENUS_ITEM_SLOT.clear();

        MENUS_ITEMS = items;
        MENUS_ITEM_SLOT = itemSlot;
    }
}
