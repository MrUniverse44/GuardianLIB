package dev.mruniverse.guardianlib.core.utils.world;

import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.SlimeWorld;
import com.grinderwolf.swm.api.world.properties.SlimeProperties;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;
import dev.mruniverse.guardianlib.core.GuardianLIB;
import org.bukkit.Bukkit;

public class SlimeWorldManagerAddon {
    private GuardianLIB plugin;
    private SlimePlugin slime;
    private SlimeLoader loader;

    public SlimeWorldManagerAddon(GuardianLIB plugin) {
        if(Bukkit.getPluginManager().getPlugin("SlimeWorldManager") != null) {
            this.plugin = plugin;
            this.slime = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");
            assert this.slime != null;
            this.loader = this.slime.getLoader(plugin.getStorage().getConfig().getString("settings.SlimeWorld.loadType"));
        } else {
            plugin.getLogs().error("The server doesn't have installed SlimeWorldManager, the plugin can give errors if an plugin uses the SlimeWorldManager API in GuardianLIB.");
        }
    }

    public void createWorld(String worldName,String worldID) {
        try {
            SlimePropertyMap props = new SlimePropertyMap();
            props.setString(SlimeProperties.DIFFICULTY, "normal");
            props.setInt(SlimeProperties.SPAWN_X, 0);
            props.setInt(SlimeProperties.SPAWN_Y, 60);
            props.setInt(SlimeProperties.SPAWN_Z, 0);
            props.setBoolean(SlimeProperties.ALLOW_ANIMALS, false);
            props.setBoolean(SlimeProperties.ALLOW_MONSTERS, false);
            props.setBoolean(SlimeProperties.PVP, true);
            SlimeWorld slimeWorld = slime.loadWorld(loader, worldName, false, props).clone(worldID);
            slime.generateWorld(slimeWorld);
        } catch (Throwable ignored) {
            plugin.getLogs().error("Can't load world: " + worldName);
        }
    }

    public void saveWorld(String worldName,String worldID) {

    }

    public void unloadworld(String name) {
        Bukkit.unloadWorld(name, false);
    }
}
//        this.main.getResetWorldSlime().unloadworld(this.uuid);
//        this.uuid = UUID.randomUUID().toString();
//        Main.getPlugin().getResetWorldSlime().createworld(this);