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

public class Holograms {
    private final HashMap<String, Hologram> listHolograms;
    //private final HashMap<String, Player[]> holoPlayers;
    private final HashMap<String, List<String>> holoLines;


    public Holograms() {
        this.listHolograms = new HashMap<>();
        //this.holoPlayers = new HashMap<>();
        this.holoLines = new HashMap<>();
    }

    public void createHologram(JavaPlugin pluginOfTheHologram, Location holoLocation, String holoName, String[] holoLines) {
        if (!Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays"))
            return;
        if (listHolograms.containsKey(holoName)) {
            listHolograms.get(holoName).delete();
            //holoPlayers.remove(holoName);
            listHolograms.remove(holoName);
            this.holoLines.remove(holoName);
        }
        if (holoLocation == null) return;
        if (holoName == null || holoLines == null) return;
        holoLocation.add(0.0D, 3.5D, 0.0D);
        Hologram hologram = HologramsAPI.createHologram(pluginOfTheHologram, holoLocation);
        List<String> lines = Utils.recolorLore(Arrays.asList(holoLines));
        VisibilityManager visibility = hologram.getVisibilityManager();
        visibility.setVisibleByDefault(true);
        //this.holoPlayers.put(holoName, PlayersOfHologram);
        this.holoLines.put(holoName,lines);
        holoAppend(hologram,lines);
        //for(Player player : PlayersOfHologram) {
        //    if(player.isOnline()) {
        //        visibility.showTo(player);
        //    }
        //}
        this.listHolograms.put(holoName, hologram);
    }

    public void reloadHolograms() {
        for (String holoName : this.listHolograms.keySet()) {
            Hologram hologram = this.listHolograms.get(holoName);
            hologram.clearLines();
            List<String> lines = this.holoLines.get(holoName);
            holoAppend(hologram,lines);
            VisibilityManager visibilityManager = hologram.getVisibilityManager();
            visibilityManager.resetVisibilityAll();
            visibilityManager.setVisibleByDefault(true);
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
        //listHolograms.clear();
    }

    private String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    //public Player[] getPlayers() {
    //    return player;
    //}
}
