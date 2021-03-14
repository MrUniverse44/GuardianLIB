package dev.mruniverse.guardianlib.core.utils;

import dev.mruniverse.guardianlib.core.GuardianLIB;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Utils {
    private final GuardianLIB plugin;
    public Utils(GuardianLIB main) {
        plugin = main;
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
}
