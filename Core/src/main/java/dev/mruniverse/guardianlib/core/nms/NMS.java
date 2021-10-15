package dev.mruniverse.guardianlib.core.nms;

import dev.mruniverse.guardianlib.core.enums.BorderColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
@SuppressWarnings("unused")
public interface NMS {
    void loadChunkListener();
    void sendTitle(Player player, int fadeIn, int stay, int fadeOut, String title, String subtitle);
    void sendActionBar(Player player, String msg);
    void playerBorder(Player player, Location borderCenter, int borderSize, BorderColor borderColor);
    void sendBossBar(Player player, String message);
    void sendBossBar(Player player, String message,float percentage);
    void spawnHologram(Player player, String holoPrivateID, String holoLineText, Location holoLocation);
    void spawnHologram(List<Player> players, String holoPrivateID, String holoLineText, Location holoLocation);
    void updateHologramText(Player player,String holoPrivateID, String holoLineText);
    void updateHologramText(List<Player> players, String holoPrivateID, String holoLineText);
    void deleteHologram(Player player,String holoPrivateID);
    void deleteHologram(List<Player> players,String holoPrivateID);
    void deleteBossBar(Player player);
    void injectPlayer(Player player);
    void resetName(Player player,List<Player> players);
    void resetName(Player player,Player[] players);
    void changeName(Player player,List<Player> players, String name);
    void changeName(Player player,Player[] players, String name);
    void setBlockData(Block block,byte data);
    void updateBlock(Location blockLocation,Material material,byte data);
    void showElderGuardian(Player player,boolean longDistance,float x,float y,float z,float offsetX,float offsetY,float offsetZ,float extra,int count);
    Location getHologramLocation(String holoPrivateID);
    boolean BossHasPlayer(Player player);
    ItemStack getItemStack(Material material, String itemName, List<String> lore);
}
