package dev.mruniverse.guardianlib.nms.v1_17R1;

import com.mojang.authlib.GameProfile;
import dev.mruniverse.guardianlib.core.GuardianLIB;
import dev.mruniverse.guardianlib.core.enums.BorderColor;
import dev.mruniverse.guardianlib.core.nms.NMS;
import dev.mruniverse.guardianlib.core.utils.Logger;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.*;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.boss.wither.EntityWither;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import org.bukkit.Location;
import org.bukkit.Material;
import net.minecraft.world.level.World;
import net.minecraft.world.level.border.WorldBorder;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;

public final class NMSHandler implements NMS {
    private final HashMap<Player, EntityWither> bossBar = new HashMap<Player, EntityWither>();
    private final HashMap<String, EntityArmorStand> hologramsID = new HashMap<String, EntityArmorStand>();

    @Override
    public void loadChunkListener() {
        GuardianLIB lib = GuardianLIB.getControl();
        lib.getServer().getPluginManager().registerEvents(new ChunkListener(lib),lib);
    }

    public void sendTitle(Player player, int fadeIn, int stay, int fadeOut, String title, String subtitle) {
        if (subtitle != null) {
            subtitle = subtitle.replaceAll("%player%", player.getName());
            subtitle = ChatColor.translateAlternateColorCodes('&', subtitle);
        }
        if (title != null) {
            title = title.replaceAll("%player%", player.getName());
            title = ChatColor.translateAlternateColorCodes('&', title);

        }
        player.sendTitle(title,subtitle,fadeIn,stay,fadeOut);
    }
    @SuppressWarnings("unused")
    public void updateBlock(Location blockLocation,Material material,byte data) {
        Logger logger = GuardianLIB.getControl().getLogs();
        logger.info("This syntax is only for 1.8 to 1.12");
        logger.info("This syntax can be used when you are developing a");
        logger.info("block system in your plugin and you're with 1.13+ libs");
        logger.info("In your project, if you want block data");
        logger.info("You can use XMaterial (included in GuardianLIB)");
        logger.info("With this syntax, you need check your server version");
        logger.info("If the server is running in 1.8 to 1.12 this syntax will work");
        logger.info("But if this server is 1.13+ only you need Block.setType((your XMaterial).parseMaterial())");
    }
    @SuppressWarnings("unused")
    public void setBlockData(org.bukkit.block.Block block,byte data) {
        Logger logger = GuardianLIB.getControl().getLogs();
        logger.info("This syntax is only for 1.8 to 1.12");
        logger.info("This syntax can be used when you are developing a");
        logger.info("block system in your plugin and you're with 1.13+ libs");
        logger.info("In your project, if you want block data");
        logger.info("You can use XMaterial (included in GuardianLIB)");
        logger.info("With this syntax, you need check your server version");
        logger.info("If the server is running in 1.8 to 1.12 this syntax will work");
        logger.info("But if this server is 1.13+ only you need Block.setType((your XMaterial).parseMaterial())");
    }
    @SuppressWarnings("unused")
    public void injectPlayer(Player player) {
        /*
         * CAN'T INJECT
         */
    }

    public void playerBorder(Player player, Location borderCenter, int borderSize, BorderColor borderColor) {
        try {
            WorldBorder worldBorder = new WorldBorder();
            CraftWorld world = ((CraftWorld)borderCenter.getWorld());
            if(world == null) return;
            worldBorder.world = world.getHandle();
            worldBorder.setSize(borderSize);
            worldBorder.setCenter(borderCenter.getX(),borderCenter.getZ());
            worldBorder.setSize(borderSize);

            worldBorder.setWarningDistance(0);
            worldBorder.setWarningTime(0);

            switch (borderColor) {
                case GREEN:
                    worldBorder.transitionSizeBetween(worldBorder.getSize() - 0.1D, worldBorder.getSize(), Long.MAX_VALUE);
                    break;
                case RED:
                    worldBorder.transitionSizeBetween(worldBorder.getSize(), worldBorder.getSize() - 1.0D, Long.MAX_VALUE);
                    break;
            }

            ((CraftPlayer) player).getHandle().b.sendPacket(new ClientboundInitializeBorderPacket(worldBorder));
        } catch (Throwable ignored) {}
    }
    public void resetName(Player player,List<Player> players) {
        CraftPlayer craftPlayer = (CraftPlayer)player;
        if(craftPlayer == null) return;
        GameProfile gameProfile = craftPlayer.getProfile();
        if(gameProfile == null) return;
        for(Player currentPlayer : players) {
            CraftPlayer currentCraftPlayer = (CraftPlayer) currentPlayer;
            if (currentCraftPlayer != null) {
                currentCraftPlayer.getHandle().b.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.e, craftPlayer.getHandle()));
                try {
                    Field nameField = GameProfile.class.getDeclaredField("name");
                    nameField.setAccessible(true);

                    Field modifiersField = Field.class.getDeclaredField("modifiers");
                    modifiersField.setAccessible(true);
                    modifiersField.setInt(nameField, nameField.getModifiers() & ~Modifier.FINAL);

                    nameField.set(gameProfile, player.getDisplayName());
                } catch (Throwable throwable) {
                    GuardianLIB.getControl().getLogs().error("(NameTag System) Can't set nameTag for " + player.getName());
                    GuardianLIB.getControl().getLogs().error(throwable);
                    return;
                }
                currentCraftPlayer.getHandle().b.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a, craftPlayer.getHandle()));
                currentCraftPlayer.getHandle().b.sendPacket(new PacketPlayOutEntityDestroy(player.getEntityId()));
                currentCraftPlayer.getHandle().b.sendPacket(new PacketPlayOutNamedEntitySpawn(craftPlayer.getHandle()));
            }
        }
    }
    public void resetName(Player player,Player[] players) {
        CraftPlayer craftPlayer = (CraftPlayer)player;
        if(craftPlayer == null) return;
        GameProfile gameProfile = craftPlayer.getProfile();
        if(gameProfile == null) return;
        for(Player currentPlayer : players) {
            CraftPlayer currentCraftPlayer = (CraftPlayer) currentPlayer;
            if (currentCraftPlayer != null) {
                currentCraftPlayer.getHandle().b.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.e, craftPlayer.getHandle()));
                try {
                    Field nameField = GameProfile.class.getDeclaredField("name");
                    nameField.setAccessible(true);

                    Field modifiersField = Field.class.getDeclaredField("modifiers");
                    modifiersField.setAccessible(true);
                    modifiersField.setInt(nameField, nameField.getModifiers() & ~Modifier.FINAL);

                    nameField.set(gameProfile, player.getDisplayName());
                } catch (Throwable throwable) {
                    GuardianLIB.getControl().getLogs().error("(NameTag System) Can't set nameTag for " + player.getName());
                    GuardianLIB.getControl().getLogs().error(throwable);
                    return;
                }
                currentCraftPlayer.getHandle().b.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a, craftPlayer.getHandle()));
                currentCraftPlayer.getHandle().b.sendPacket(new PacketPlayOutEntityDestroy(player.getEntityId()));
                currentCraftPlayer.getHandle().b.sendPacket(new PacketPlayOutNamedEntitySpawn(craftPlayer.getHandle()));
            }
        }
    }
    public void changeName(Player player,List<Player> players, String name){
        CraftPlayer craftPlayer = (CraftPlayer)player;
        if(craftPlayer == null) return;
        GameProfile gameProfile = craftPlayer.getProfile();
        if(gameProfile == null) return;
        for(Player currentPlayer : players) {
            CraftPlayer currentCraftPlayer = (CraftPlayer) currentPlayer;
            if (currentCraftPlayer != null) {
                currentCraftPlayer.getHandle().b.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.e, craftPlayer.getHandle()));
                try {
                    Field nameField = GameProfile.class.getDeclaredField("name");
                    nameField.setAccessible(true);

                    Field modifiersField = Field.class.getDeclaredField("modifiers");
                    modifiersField.setAccessible(true);
                    modifiersField.setInt(nameField, nameField.getModifiers() & ~Modifier.FINAL);

                    nameField.set(gameProfile, name);
                } catch (Throwable throwable) {
                    GuardianLIB.getControl().getLogs().error("(NameTag System) Can't set nameTag for " + player.getName());
                    GuardianLIB.getControl().getLogs().error(throwable);
                    return;
                }
                currentCraftPlayer.getHandle().b.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a, craftPlayer.getHandle()));
                currentCraftPlayer.getHandle().b.sendPacket(new PacketPlayOutEntityDestroy(player.getEntityId()));
                currentCraftPlayer.getHandle().b.sendPacket(new PacketPlayOutNamedEntitySpawn(craftPlayer.getHandle()));
            }
        }
    }
    public void changeName(Player player,Player[] players, String name){
        CraftPlayer craftPlayer = (CraftPlayer)player;
        if(craftPlayer == null) return;
        GameProfile gameProfile = craftPlayer.getProfile();
        if(gameProfile == null) return;
        for(Player currentPlayer : players) {
            CraftPlayer currentCraftPlayer = (CraftPlayer) currentPlayer;
            if (currentCraftPlayer != null) {
                currentCraftPlayer.getHandle().b.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.e, craftPlayer.getHandle()));
                try {
                    Field nameField = GameProfile.class.getDeclaredField("name");
                    nameField.setAccessible(true);

                    Field modifiersField = Field.class.getDeclaredField("modifiers");
                    modifiersField.setAccessible(true);
                    modifiersField.setInt(nameField, nameField.getModifiers() & ~Modifier.FINAL);

                    nameField.set(gameProfile, name);
                } catch (Throwable throwable) {
                    GuardianLIB.getControl().getLogs().error("(NameTag System) Can't set nameTag for " + player.getName());
                    GuardianLIB.getControl().getLogs().error(throwable);
                    return;
                }
                currentCraftPlayer.getHandle().b.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a, craftPlayer.getHandle()));
                currentCraftPlayer.getHandle().b.sendPacket(new PacketPlayOutEntityDestroy(player.getEntityId()));
                currentCraftPlayer.getHandle().b.sendPacket(new PacketPlayOutNamedEntitySpawn(craftPlayer.getHandle()));
            }
        }
    }
    public Location getHologramLocation(String holoPrivateID) {
        return hologramsID.get(holoPrivateID).getBukkitEntity().getLocation();
    }
    public void spawnHologram(Player player,String holoPrivateID,String holoLineText,Location holoLocation) {
        EntityArmorStand armorStand = new EntityArmorStand((World)holoLocation.getWorld(), holoLocation.getX(), holoLocation.getY(), holoLocation.getZ());

        armorStand.setNoGravity(true);
        armorStand.setCustomName(IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + holoLineText + "\"}"));
        armorStand.setCustomNameVisible(true);
        armorStand.setInvisible(true);
        armorStand.setSmall(true);
        armorStand.setBasePlate(false);

        PacketPlayOutSpawnEntityLiving spawnPacket = new PacketPlayOutSpawnEntityLiving(armorStand);
        ((CraftPlayer) player).getHandle().b.sendPacket(spawnPacket);
        hologramsID.put(holoPrivateID,armorStand);
    }
    public void spawnHologram(List<Player> players,String holoPrivateID,String holoLineText,Location holoLocation) {
        CraftWorld world = (CraftWorld) holoLocation.getWorld();
        if(world == null) {
            GuardianLIB.getInstance().getLogs().info("Can't spawn a hologram, NotFoundWorldException");
            return;
        }
        EntityArmorStand armorStand = new EntityArmorStand(world.getHandle(), holoLocation.getX(), holoLocation.getY(), holoLocation.getZ());

        armorStand.setNoGravity(true);
        armorStand.setCustomName(IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + holoLineText + "\"}"));
        armorStand.setCustomNameVisible(true);
        armorStand.setInvisible(true);
        armorStand.setSmall(true);
        armorStand.setBasePlate(false);

        PacketPlayOutSpawnEntityLiving spawnPacket = new PacketPlayOutSpawnEntityLiving(armorStand);
        for(Player player : players) {
            try {
                ((CraftPlayer) player).getHandle().b.sendPacket(spawnPacket);
            }catch (Throwable ignored) {}
        }
        hologramsID.put(holoPrivateID,armorStand);
    }
    public void updateHologramText(Player player,String holoPrivateID,String holoLineText) {
        if(!hologramsID.containsKey(holoPrivateID)) {
            GuardianLIB.getControl().getLogs().info("(Hologram System) HoloPrivateID: " + holoPrivateID + " doesn't exists.");
            return;
        }
        EntityArmorStand armorStand = hologramsID.get(holoPrivateID);
        armorStand.setCustomName(IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + holoLineText + "\"}"));
        PacketPlayOutEntityMetadata metaPacket = new PacketPlayOutEntityMetadata(armorStand.getId(), armorStand.getDataWatcher(), true);
        ((CraftPlayer) player).getHandle().b.sendPacket(metaPacket);
    }
    public void updateHologramText(List<Player> players,String holoPrivateID,String holoLineText) {
        if(!hologramsID.containsKey(holoPrivateID)) {
            GuardianLIB.getControl().getLogs().info("(Hologram System) HoloPrivateID: " + holoPrivateID + " doesn't exists.");
            return;
        }
        EntityArmorStand armorStand = hologramsID.get(holoPrivateID);
        armorStand.setCustomName(IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + holoLineText + "\"}"));
        PacketPlayOutEntityMetadata metaPacket = new PacketPlayOutEntityMetadata(armorStand.getId(), armorStand.getDataWatcher(), true);
        for(Player player : players) {
            try {
                ((CraftPlayer) player).getHandle().b.sendPacket(metaPacket);
            }catch (Throwable ignored) {}
        }
    }
    public void deleteHologram(Player player,String holoPrivateID) {
        if(!hologramsID.containsKey(holoPrivateID)) return;
        EntityArmorStand armorStand = hologramsID.remove(holoPrivateID);
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(armorStand.getId());
        ((CraftPlayer) player).getHandle().b.sendPacket(packet);
    }
    public void deleteHologram(List<Player> players,String holoPrivateID) {
        if(!hologramsID.containsKey(holoPrivateID)) return;
        EntityArmorStand armorStand = hologramsID.remove(holoPrivateID);
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(armorStand.getId());
        for(Player player : players) {
            try {
                ((CraftPlayer) player).getHandle().b.sendPacket(packet);
            }catch (Throwable ignored) {}
        }
    }

    public void sendActionBar(Player player, String msg) {
        String toBC = ChatColor.translateAlternateColorCodes('&', msg);
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(toBC));
    }
    public void sendBossBar(Player player, String message) {
        if(!BossHasPlayer(player)) {
            bossBar.put(player,new EntityWither(EntityTypes.aZ,(World)player.getWorld()));
        }
        Location witherLocation = getWitherLocation(player.getLocation());
        getBossBar(player).setCustomName(IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}"));
        float life = (100 * getBossBar(player).getMaxHealth());
        getBossBar(player).setHealth(life);
        getBossBar(player).setInvisible(true);
        getBossBar(player).setLocation(witherLocation.getX(), witherLocation.getY(), witherLocation.getZ(), 0, 0);
        PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(getBossBar(player));
        ((CraftPlayer) player).getHandle().b.sendPacket(packet);
    }
    public void sendBossBar(Player player, String message,float percentage) {
        if(!BossHasPlayer(player)) {
            bossBar.put(player,new EntityWither(EntityTypes.aZ,(World)player.getWorld()));
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
        ((CraftPlayer) player).getHandle().b.sendPacket(packet);
    }
    public void deleteBossBar(Player player) {
        if(!BossHasPlayer(player)) return;
        EntityWither wither = bossBar.remove(player);
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(wither.getId());
        ((CraftPlayer) player).getHandle().b.sendPacket(packet);
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
