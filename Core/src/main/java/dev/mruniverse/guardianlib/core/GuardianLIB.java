package dev.mruniverse.guardianlib.core;

import dev.mruniverse.guardianlib.core.enums.NMSenum;
import dev.mruniverse.guardianlib.core.files.FileStorage;
import dev.mruniverse.guardianlib.core.nms.NMS;
import dev.mruniverse.guardianlib.core.utils.ExternalLogger;
import dev.mruniverse.guardianlib.core.utils.Logger;
import dev.mruniverse.guardianlib.core.utils.Utils;
import dev.mruniverse.guardianlib.core.utils.world.SlimeWorldManagerAddon;
import dev.mruniverse.guardianlib.core.utils.world.WorldController;
import org.bukkit.plugin.java.JavaPlugin;
@SuppressWarnings("unused")
public final class GuardianLIB extends JavaPlugin {
    private NMS nmsHandler;
    private static GuardianLIB instance;
    private SlimeWorldManagerAddon slimeWorldManager;
    private WorldController worldManager;
    private boolean hasPAPI = false;
    private FileStorage fileStorage;
    private Logger logger;
    private Utils utils;

    @Override
    public void onEnable() {
        instance = this;
        hasPAPI = getServer().getPluginManager().isPluginEnabled("PlaceholderAPI");
        utils = new Utils(this);
        slimeWorldManager = new SlimeWorldManagerAddon(this);
        worldManager = new WorldController(this);
        logger = new Logger(this);
        fileStorage = new FileStorage(this);
        nmsSetup();
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

    public ExternalLogger initLogger(JavaPlugin plugin,String pluginName,String hidePackage) {
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
    public boolean hasPAPI() {
        return hasPAPI;
    }
    public static GuardianLIB getInstance() {
        return instance;
    }
    public static GuardianLIB getControl() {
        return instance;
    }
    public Logger getLogs() {
        return logger;
    }

}
