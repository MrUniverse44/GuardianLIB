package dev.mruniverse.guardianlib.core.utils;

import dev.mruniverse.guardianlib.core.GuardianLIB;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
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
                    title = PlaceholderAPI.setPlaceholders(player, title);
                }
                if(subtitle != null) {
                    subtitle = PlaceholderAPI.setPlaceholders(player, subtitle);
                }
            }
            plugin.getNMS().sendTitle(player,fadeInTime,showTime,fadeOutTime,title,subtitle);

        } catch (Throwable throwable) {
            plugin.getLogs().error("Can't send title for " + player.getName() + ".");
            plugin.getLogs().error(throwable);
        }
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
