package dev.mruniverse.guardianlib.core;

import dev.mruniverse.guardianlib.core.enums.NMSenum;
import dev.mruniverse.guardianlib.core.nms.NMS;
import dev.mruniverse.guardianlib.core.utils.Logger;
import dev.mruniverse.guardianlib.core.utils.Utils;
import org.bukkit.plugin.java.JavaPlugin;

public final class GuardianLIB extends JavaPlugin {
    private NMS nmsHandler;
    private static GuardianLIB instance;
    private boolean hasPAPI = false;
    private Logger logger;
    private Utils utils;

    @Override
    public void onEnable() {
        instance = this;
        hasPAPI = getServer().getPluginManager().isPluginEnabled("PlaceholderAPI");
        utils = new Utils(this);
        logger = new Logger(this);
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

    public NMS getNMS() {
        return nmsHandler;
    }
    @SuppressWarnings("unused")
    public Utils getUtils() {
        return utils;
    }

    public boolean hasPAPI() {
        return hasPAPI;
    }

    public GuardianLIB getInstance() {
        return instance;
    }
    public GuardianLIB getControl() {
        return instance;
    }

    public Logger getLogs() {
        return logger;
    }

}
