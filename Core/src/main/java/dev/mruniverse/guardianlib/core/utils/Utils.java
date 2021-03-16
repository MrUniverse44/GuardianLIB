package dev.mruniverse.guardianlib.core.utils;

import dev.mruniverse.guardianlib.core.GuardianLIB;
import dev.mruniverse.guardianlib.core.utils.xseries.XEnchantment;
import dev.mruniverse.guardianlib.core.utils.xseries.XMaterial;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class Utils {
    private final GuardianLIB plugin;
    public Utils(GuardianLIB main) {
        plugin = main;
    }
    public void sendMessage(Player player,String message) {
        if(message == null) message = "Unknown Message";
        if(plugin.hasPAPI()) {
            message = PlaceholderAPI.setPlaceholders(player,message);
        }
        message = ChatColor.translateAlternateColorCodes('&',message);
        player.sendMessage(message);
    }
    public void sendMessage(CommandSender sender, String message) {
        if(message == null) message = "Unknown Message";
        message = ChatColor.translateAlternateColorCodes('&',message);
        sender.sendMessage(message);
    }
    public void sendCenteredMessage(Player player,String message) {
        if(message == null) message = "Unknown Message";
        if(plugin.hasPAPI()) {
            message = PlaceholderAPI.setPlaceholders(player,message);
        }
        message = ChatColor.translateAlternateColorCodes('&',message);
        player.sendMessage(CenterText.sendToCenter(message));
    }
    public void sendCenteredMessage(CommandSender sender,String message) {
        if(message == null) message = "Unknown Message";
        message = ChatColor.translateAlternateColorCodes('&',message);
        sender.sendMessage(CenterText.sendToCenter(message));
    }
    public void sendTitle(Player player, int fadeInTime, int showTime, int fadeOutTime, String title, String subtitle) {
        try {
            if(plugin.hasPAPI()) {
                if(title != null) {
                    if(!title.contains("<empty>")) {
                        title = PlaceholderAPI.setPlaceholders(player, title);
                    } else {
                        title = null;
                    }
                }
                if(subtitle != null) {
                    if(!subtitle.contains("<empty>")) {
                        subtitle = PlaceholderAPI.setPlaceholders(player, subtitle);
                    } else {
                        subtitle = null;
                    }
                }
            }
            plugin.getNMS().sendTitle(player,fadeInTime,showTime,fadeOutTime,title,subtitle);

        } catch (Throwable throwable) {
            plugin.getLogs().error("Can't send title for " + player.getName() + ".");
            plugin.getLogs().error(throwable);
        }
    }
    public boolean isNewVersion() {
        return versionVerificator.isNewVersion();
    }
    public String getStringFromLocation(Location location) {
        try {
            World currentWorld = location.getWorld();
            String worldName = "world";
            if(currentWorld != null) worldName = location.getWorld().getName();
            return worldName + "," + location.getX() + "," + location.getY() + "," + location.getZ() + "," + location.getYaw() + "," + location.getPitch();
        }catch (Throwable throwable) {
            plugin.getLogs().error("Can't get String from location " + location.toString());
            plugin.getLogs().error(throwable);
        }
        return null;
    }
    public Location getLocationFromString(String location) {
        if(!location.equalsIgnoreCase("notSet")) {
            String[] loc = location.split(",");
            World w = Bukkit.getWorld(loc[0]);
            if(w != null) {
                double x = Double.parseDouble(loc[1]);
                double y = Double.parseDouble(loc[2]);
                double z = Double.parseDouble(loc[3]);
                float yaw = Float.parseFloat(loc[4]);
                float pitch = Float.parseFloat(loc[5]);
                return new Location(w, x, y, z, yaw, pitch);
            }
            plugin.getLogs().error("Can't get world named: " + loc[0]);
            return null;
        }
        return null;
    }
    public void sendBossBar(Player player, String message) {
        if (player == null || message == null) return;
        if(plugin.hasPAPI()) { message = PlaceholderAPI.setPlaceholders(player,message); }
        message = color(message);
        plugin.getNMS().sendBossBar(player,message);
    }
    public void sendBossBar(Player player, String message, float percentage) {
        if (player == null || message == null) return;
        if(plugin.hasPAPI()) { message = PlaceholderAPI.setPlaceholders(player,message); }
        message = color(message);
        plugin.getNMS().sendBossBar(player,message,percentage);
    }
    public void sendActionbar(Player player, String message) {
        if (player == null || message == null) return;
        if(plugin.hasPAPI()) { message = PlaceholderAPI.setPlaceholders(player,message); }
        message = color(message);
        plugin.getNMS().sendActionBar(player,message);
    }

    public void deleteBossBar(Player player) {
        plugin.getNMS().deleteBossBar(player);
    }

    private String color(String message) {
        return ChatColor.translateAlternateColorCodes('&',message);
    }

    public void playSound(Player player,String soundName) {
        soundName = soundName.toUpperCase();
        try{
            player.playSound(player.getLocation(), Sound.valueOf(soundName), 5F,1F);
        }catch (Throwable ignored){
            plugin.getLogs().info("Sound: " + soundName + " doesn't exist.");
        }
    }
    public void playSound(Player player,String soundName,float volume) {
        soundName = soundName.toUpperCase();
        try{
            player.playSound(player.getLocation(), Sound.valueOf(soundName), volume,1F);
        }catch (Throwable ignored){
            plugin.getLogs().info("Sound: " + soundName + " doesn't exist.");
        }
    }
    public boolean checkValidSound(String soundName) {
        soundName = soundName.toUpperCase();
        return EnumUtils.isValidEnum(Sound.class,soundName);
    }
    public void playSound(Player player,String soundName,float volume,float pitch) {
        soundName = soundName.toUpperCase();
        try{
            player.playSound(player.getLocation(), Sound.valueOf(soundName), volume, pitch);
        }catch (Throwable ignored){
            plugin.getLogs().info("Sound: " + soundName + " doesn't exist.");
        }
    }

    public ItemStack getItem(XMaterial xItem, String name, List<String> lore) {
        ItemStack itemToReturn = xItem.parseItem();
        if(itemToReturn != null) {
            ItemMeta ReturnMeta = itemToReturn.getItemMeta();
            if(ReturnMeta != null) {
                ReturnMeta.setDisplayName(recolor(name));
                ReturnMeta.setLore(recolorLore(lore));
                itemToReturn.setItemMeta(ReturnMeta);
                return itemToReturn;
            }
            return itemToReturn;
        }
        return plugin.getNMS().getItemStack(xItem.parseMaterial(), recolor(name), recolorLore(lore));
    }
    @SuppressWarnings("ConstantConditions")
    public ItemStack getEnchantmentList(ItemStack item, List<String> enchantments, String path) {
        for(String enchant : enchantments) {
            try {
                item = XEnchantment.addEnchantFromString(item, enchant);
            } catch(Throwable throwable) {
                plugin.getLogs().error("Can't add Enchantment: " + enchant);
            }
        }
        return item;
    }

    public static String recolor(String message) {
        return ChatColor.translateAlternateColorCodes('&',message);
    }
    public static List<String> recolorLore(List<String> loreToRecolor) {
        List<String> recolored = new ArrayList<>();
        for(String color : loreToRecolor) {
            recolored.add(ChatColor.translateAlternateColorCodes('&',color));
        }
        return recolored;
    }

}
