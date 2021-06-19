package dev.mruniverse.guardianlib.core;

import dev.mruniverse.guardianlib.core.enums.NMSenum;
import dev.mruniverse.guardianlib.core.files.FileStorage;
import dev.mruniverse.guardianlib.core.listeners.HoloListener;
import dev.mruniverse.guardianlib.core.listeners.JoinListener;
import dev.mruniverse.guardianlib.core.nms.NMS;
import dev.mruniverse.guardianlib.core.schematics.SchematicManager;
import dev.mruniverse.guardianlib.core.utils.ExternalLogger;
import dev.mruniverse.guardianlib.core.utils.Logger;
import dev.mruniverse.guardianlib.core.utils.Utils;
import dev.mruniverse.guardianlib.core.utils.world.SlimeWorldManagerAddon;
import dev.mruniverse.guardianlib.core.utils.world.WorldController;
import org.bukkit.entity.ArmorStand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public final class GuardianLIB extends JavaPlugin {
    private NMS nmsHandler;
    private static GuardianLIB instance;
    private SlimeWorldManagerAddon slimeWorldManager;
    private WorldController worldManager;
    private boolean hasPAPI = false;
    public HashMap<Integer, List<ArmorStand>> armorStands;
    private boolean hasFAWE = false;
    private SchematicManager schematicManager;
    private FileStorage fileStorage;
    private Logger logger;
    private Utils utils;
    private int hologramsLoaded;

    @Override
    public void onEnable() {
        instance = this;
        armorStands = new HashMap<>();
        logger = new Logger(this);
        hasPAPI = getServer().getPluginManager().isPluginEnabled("PlaceholderAPI");
        hasFAWE = getServer().getPluginManager().isPluginEnabled("FastAsyncWorldEdit");
        utils = new Utils(this);
        hologramsLoaded = 0;
        if(getServer().getPluginManager().isPluginEnabled("WorldEdit") || hasFAWE) {
            schematicManager = new SchematicManager(this,true);
        } else {
            logger.info("Schematics System disabled: &aNo World Edit plugin found &e& &aNo FAWE plugin found&e.");
            schematicManager = new SchematicManager(this,false);
        }
        fileStorage = new FileStorage(this);
        worldManager = new WorldController(this);
        slimeWorldManager = new SlimeWorldManagerAddon(this);
        getServer().getPluginManager().registerEvents(new JoinListener(this),this);
        getServer().getPluginManager().registerEvents(new HoloListener(this),this);
        nmsSetup();
    }

    @Override
    public void onDisable() {
        for(Map.Entry<Integer,List<ArmorStand>> entry : armorStands.entrySet()) {
            unloadArmor(entry.getValue());
            logger.info("Unloading global-holograms from ID: GH-" + entry.getKey());
        }
    }
    private void unloadArmor(List<ArmorStand> armors) {
        for(ArmorStand armor : armors) {
            armor.remove();
        }
    }
    public HashMap<Integer,List<ArmorStand>> getArmorStandsUsingPrivateID() {
        return armorStands;
    }
    private void nmsSetup() {
        try {
            nmsHandler = (NMS) Class.forName("dev.mruniverse.guardianlib.nms." + NMSenum.getCurrent() + ".NMSHandler").getConstructor(new Class[0]).newInstance(new Object[0]);
            getLogs().info("Successfully connected with version: " + NMSenum.getCurrent() + ", the plugin can work correctly. If you found an issue please report to the developer.");
        }catch (Throwable throwable) {
            getLogs().error("Can't initialize NMS, unsupported version: " + NMSenum.getCurrent());
            getLogs().error(throwable);
        }
    }

    public static ExternalLogger initLogger(JavaPlugin plugin,String pluginName,String hidePackage) {
        return new ExternalLogger(plugin,pluginName,hidePackage);
    }

    public NMS getNMS() {
        return nmsHandler;
    }
    public Utils getUtils() {
        return utils;
    }
    public FileStorage getStorage() {
        return fileStorage;
    }
    public SlimeWorldManagerAddon getSlime() { return slimeWorldManager; }
    public WorldController getWorldManager() { return worldManager; }
    public SchematicManager getSchematics() { return schematicManager; }
    public boolean hasPAPI() {
        return hasPAPI;
    }
    public boolean hasFAWE() { return hasFAWE; }
    public static GuardianLIB getInstance() {
        return instance;
    }
    public static GuardianLIB getControl() {
        return instance;
    }
    public Logger getLogs() {
        return logger;
    }
    public int getHologramsLoaded() { return hologramsLoaded; }
    public void setHologramsLoaded(int newSize) { hologramsLoaded = newSize; }

}
