package dev.mruniverse.guardianlib.core.holograms;

import dev.mruniverse.guardianlib.core.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.VisibilityManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@SuppressWarnings("unused")
public class PersonalHologram {
    private final HashMap<String, Hologram> listHolograms;
    private final Player player;
    private final JavaPlugin plugin;
    private final HashMap<String, List<String>> holoLines;


    public PersonalHologram(JavaPlugin plugin, Player player) {
        this.listHolograms = new HashMap<>();
        this.player = player;
        this.plugin = plugin;
        this.holoLines = new HashMap<>();
    }

    public void createHologram(String holoName,Location holoLocation, String[] holoLines) {
        if (!Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays"))
            return;
        if (listHolograms.containsKey(holoName)) {
            listHolograms.get(holoName).delete();
            listHolograms.remove(holoName);
            this.holoLines.remove(holoName);
        }
        if (holoLocation == null) return;
        if (holoName == null || holoLines == null) return;
        holoLocation.add(0.0D, 3.5D, 0.0D);
        Hologram hologram = HologramsAPI.createHologram(plugin, holoLocation);
        List<String> lines = Utils.recolorLore(Arrays.asList(holoLines));
        VisibilityManager visibility = hologram.getVisibilityManager();
        visibility.setVisibleByDefault(false);
        this.holoLines.put(holoName,lines);
        holoAppend(hologram,lines);
        visibility.showTo(player);
        this.listHolograms.put(holoName, hologram);
    }

    public void updateLines(String holoName,String[] holoLines) {
        Hologram hologram = this.listHolograms.get(holoName);
        hologram.clearLines();
        List<String> lines = Utils.recolorLore(Arrays.asList(holoLines));
        this.holoLines.put(holoName,lines);
        holoAppend(hologram,lines);
        VisibilityManager visibilityManager = hologram.getVisibilityManager();
        visibilityManager.resetVisibility(player);
        visibilityManager.showTo(player);
    }

    public void updateLine(String holoName,int line,String text) {
        Hologram hologram = this.listHolograms.get(holoName);
        text = color(text.replace("%online%",Bukkit.getOnlinePlayers().size() + "")
                .replace("%max%",Bukkit.getMaxPlayers() + ""));
        hologram.insertTextLine(line,text);
        hologram.removeLine(line+1);
        List<String> lines = holoLines.get(holoName);
        lines.set(line-1,text);
        holoLines.put(holoName,lines);
        VisibilityManager visibilityManager = hologram.getVisibilityManager();
        visibilityManager.resetVisibility(player);
        visibilityManager.showTo(player);
    }

    public void reloadHologram(String holoName) {
        Hologram hologram = this.listHolograms.get(holoName);
        hologram.clearLines();
        List<String> lines = this.holoLines.get(holoName);
        holoAppend(hologram,lines);
        VisibilityManager visibilityManager = hologram.getVisibilityManager();
        visibilityManager.resetVisibility(player);
        visibilityManager.showTo(player);
    }

    public void reloadHolograms() {
        for (String holoName : this.listHolograms.keySet()) {
            Hologram hologram = this.listHolograms.get(holoName);
            hologram.clearLines();
            List<String> lines = this.holoLines.get(holoName);
            holoAppend(hologram,lines);
            VisibilityManager visibilityManager = hologram.getVisibilityManager();
            visibilityManager.resetVisibility(player);
            visibilityManager.showTo(player);
        }
    }
    public void holoAppend(Hologram hologram,List<String> lines) {
        for(String line : lines) {
            line = color(line.replace("%online%",Bukkit.getOnlinePlayers().size() + "")
                    .replace("%max%",Bukkit.getMaxPlayers() + ""));
            hologram.appendTextLine(line);
        }
    }
    public void deleteHolograms() {
        if (!Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays"))
            return;
        for (Map.Entry<String,Hologram> entry : listHolograms.entrySet()) {
            entry.getValue().delete();
        }
    }

    private String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public Player getPlayers() {
        return player;
    }
}

