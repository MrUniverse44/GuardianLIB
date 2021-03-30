package dev.mruniverse.guardianlib.core.schematics;

import dev.mruniverse.guardianlib.core.GuardianLIB;
import org.bukkit.Location;

import java.io.File;
@SuppressWarnings("unused")
public class SchematicManager {
    private final GuardianLIB plugin;
    private FAWEController faweController;
    private WEController weController;
    public SchematicManager(GuardianLIB plugin,boolean load) {
        this.plugin = plugin;

        if(load) {
            faweController = new FAWEController();
            weController = new WEController();
            return;
        }
        faweController = null;
        weController = null;
    }
    public void loadAgain() {
        faweController = new FAWEController();
        weController = new WEController();
    }
    public void pasteSchematic(File Schematic, Location location) {
        if(faweController == null && weController == null) {
            plugin.getLogs().error("This server doesn't have installed WorldEdit plugins.");
            return;
        }
        if(plugin.hasFAWE() && faweController != null) {
            faweController.pasteSchematic(Schematic, location);
            return;
        }
        weController.pasteSchematic(Schematic,location);
    }
    public FAWEController getFAWE() {
        return faweController;
    }
    public WEController getWE() {
        return weController;
    }
}
