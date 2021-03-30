package dev.mruniverse.guardianlib.nms.v1_14_R1;

import dev.mruniverse.guardianlib.core.GuardianLIB;
import dev.mruniverse.guardianlib.core.enums.BorderColor;
import dev.mruniverse.guardianlib.core.nms.NMS;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_14_R1.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public final class NMSHandler implements NMS {
    private final HashMap<Player, EntityWither> bossBar = new HashMap<>();
    private final HashMap<String,EntityArmorStand> hologramsID = new HashMap<>();
    public void sendTitle(Player player, int fadeIn, int stay, int fadeOut, String title, String subtitle) {
        PlayerConnection pConn = (((CraftPlayer)player).getHandle()).playerConnection;
        PacketPlayOutTitle pTitleInfo = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, null, fadeIn, stay, fadeOut);
        pConn.sendPacket(pTitleInfo);
        if (subtitle != null) {
            subtitle = subtitle.replaceAll("%player%", player.getName());
            subtitle = ChatColor.translateAlternateColorCodes('&', subtitle);
            IChatBaseComponent iComp = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + subtitle + "\"}");
            PacketPlayOutTitle pSubtitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, iComp);
            pConn.sendPacket(pSubtitle);
        }
        if (title != null) {
            title = title.replaceAll("%player%", player.getName());
            title = ChatColor.translateAlternateColorCodes('&', title);
            IChatBaseComponent iComp = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + title + "\"}");
            PacketPlayOutTitle pTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, iComp);
            pConn.sendPacket(pTitle);
        }
    }
    public void injectPlayer(Player player) {

    }

    public void spawnHologram(Player player,String holoPrivateID,String holoLineText,Location holoLocation) {
        EntityArmorStand armorStand = new EntityArmorStand(((CraftWorld) Objects.requireNonNull(holoLocation.getWorld())).getHandle(), holoLocation.getX(), holoLocation.getY(), holoLocation.getZ());

        armorStand.setNoGravity(true);
        armorStand.setCustomName(IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + holoLineText + "\"}"));
        armorStand.setCustomNameVisible(true);
        armorStand.setInvisible(true);
        armorStand.setSmall(true);
        armorStand.setBasePlate(false);

        PacketPlayOutSpawnEntityLiving spawnPacket = new PacketPlayOutSpawnEntityLiving(armorStand);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(spawnPacket);
        hologramsID.put(holoPrivateID,armorStand);
    }
    public Location getHologramLocation(String holoPrivateID) {
        return hologramsID.get(holoPrivateID).getBukkitEntity().getLocation();
    }
    public void playerBorder(Player player, Location borderCenter, int borderSize, BorderColor borderColor) {
        try {
            WorldBorder worldBorder;
            worldBorder = new WorldBorder();
            CraftWorld world = ((CraftWorld)borderCenter.getWorld());
            if(world == null) return;
            worldBorder.world = world.getHandle();
            worldBorder.setSize(borderSize);
            worldBorder.setCenter(borderCenter.getX(),borderCenter.getZ());
            switch (borderColor) {
                case GREEN:
                    worldBorder.transitionSizeBetween(worldBorder.getSize() - 0.1D, worldBorder.getSize(), Long.MAX_VALUE);
                    break;
                case RED:
                    worldBorder.transitionSizeBetween(worldBorder.getSize(), worldBorder.getSize() - 1.0D, Long.MAX_VALUE);
                    break;
            }
            PacketPlayOutWorldBorder packetPlayOutWorldBorder = new PacketPlayOutWorldBorder(worldBorder, PacketPlayOutWorldBorder.EnumWorldBorderAction.INITIALIZE);
            (((CraftPlayer)player).getHandle()).playerConnection.sendPacket(packetPlayOutWorldBorder);
        } catch (Throwable ignored) {}
    }

    public void updateHologramText(Player player,String holoPrivateID,String holoLineText) {
        if(!hologramsID.containsKey(holoPrivateID)) {
            GuardianLIB.getControl().getLogs().info("(GlobalHologram System) HoloPrivateID: " + holoPrivateID + " doesn't exists.");
            return;
        }
        EntityArmorStand armorStand = hologramsID.get(holoPrivateID);
        armorStand.setCustomName(IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + holoLineText + "\"}"));
        PacketPlayOutEntityMetadata metaPacket = new PacketPlayOutEntityMetadata(armorStand.getId(), armorStand.getDataWatcher(), true);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(metaPacket);
    }
    public void deleteHologram(Player player,String holoPrivateID) {
        if(!hologramsID.containsKey(holoPrivateID)) return;
        EntityArmorStand armorStand = hologramsID.remove(holoPrivateID);
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(armorStand.getId());
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
    }

    public void sendActionBar(Player player, String msg) {
        String toBC = ChatColor.translateAlternateColorCodes('&', msg);
        IChatBaseComponent icbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + toBC + "\"}");
        PacketPlayOutChat bar = new PacketPlayOutChat(icbc, ChatMessageType.GAME_INFO);
        (((CraftPlayer)player).getHandle()).playerConnection.sendPacket(bar);
    }
    public void sendBossBar(Player player, String message) {
        if(!BossHasPlayer(player)) {
            bossBar.put(player,new EntityWither(EntityTypes.WITHER,((CraftWorld)player.getWorld()).getHandle()));
        }
        Location witherLocation = getWitherLocation(player.getLocation());
        getBossBar(player).setCustomName(IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}"));
        float life = (100 * getBossBar(player).getMaxHealth());
        getBossBar(player).setHealth(life);
        getBossBar(player).setInvisible(true);
        getBossBar(player).setLocation(witherLocation.getX(), witherLocation.getY(), witherLocation.getZ(), 0, 0);
        PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(getBossBar(player));
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
    }
    public void sendBossBar(Player player, String message,float percentage) {
        if(!BossHasPlayer(player)) {
            bossBar.put(player,new EntityWither(EntityTypes.WITHER,((CraftWorld)player.getWorld()).getHandle()));
        }
        if (percentage <= 0) {
            percentage = (float) 0.001;
        }
        Location witherLocation = getWitherLocation(player.getLocation());
        getBossBar(player).setCustomName(IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}"));
        float life = (percentage * getBossBar(player).getMaxHealth());
        getBossBar(player).setHealth(life);
        getBossBar(player).setInvisible(true);
        getBossBar(player).setLocation(witherLocation.getX(), witherLocation.getY(), witherLocation.getZ(), 0, 0);
        PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(getBossBar(player));
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
    }
    public void deleteBossBar(Player player) {
        if(!BossHasPlayer(player)) return;
        EntityWither wither = bossBar.remove(player);
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(wither.getId());
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
    }
    public boolean BossHasPlayer(Player player) {
        return bossBar.containsKey(player);
    }

    public ItemStack getItemStack(Material material, String itemName, List<String> lore) {
        ItemStack addItem = new ItemStack(material, 1);
        ItemMeta addItemMeta = addItem.getItemMeta();
        assert addItemMeta != null;
        addItemMeta.setDisplayName(itemName);
        addItemMeta.setLore(lore);
        addItemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        addItemMeta.addItemFlags(ItemFlag.HIDE_DESTROYS);
        addItemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        addItemMeta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        addItemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        addItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        addItem.setItemMeta(addItemMeta);
        return addItem;
    }

    private EntityWither getBossBar(Player player) {
        return bossBar.get(player);
    }
    private Location getWitherLocation(Location playerLocation) {
        return playerLocation.add(playerLocation.getDirection().multiply(60));
    }
}

