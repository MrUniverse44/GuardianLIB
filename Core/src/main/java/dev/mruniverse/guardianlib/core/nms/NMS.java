package dev.mruniverse.guardianlib.core.nms;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
@SuppressWarnings("unused")
public interface NMS {
    void sendTitle(Player player, int fadeIn, int stay, int fadeOut, String title, String subtitle);
    void sendActionBar(Player player, String msg);
    void sendBossBar(Player player, String message);
    void sendBossBar(Player player, String message,float percentage);
    void deleteBossBar(Player player);
    boolean BossHasPlayer(Player player);
    ItemStack getItemStack(Material material, String itemName, List<String> lore);
}
