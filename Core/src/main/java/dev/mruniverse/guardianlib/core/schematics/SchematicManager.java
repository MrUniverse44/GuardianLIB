package dev.mruniverse.guardianlib.core.schematics;

import dev.mruniverse.guardianlib.core.GuardianLIB;
import dev.mruniverse.guardianlib.core.enums.NMSenum;
import org.bukkit.Location;

import java.io.File;
@SuppressWarnings("unused")
public class SchematicManager {
    private final GuardianLIB plugin;
    private FAWEController faweController;
    private WorldEditController worldEditController;
    public SchematicManager(GuardianLIB plugin,boolean load) {
        this.plugin = plugin;

        if(load) {
            faweController = new FAWEController();
            worldEditController = new WEController();
            return;
        }
        faweController = null;
        worldEditController = null;
    }
    public void loadAgain() {
        faweController = new FAWEController();
        if(NMSenum.isBetween(NMSenum.v1_8_R1,NMSenum.v1_12_R1)) {
            worldEditController = new WEController();
            return;
        }
    }
    public void pasteSchematic(File Schematic, Location location) {
        if(faweController == null && worldEditController == null) {
            plugin.getLogs().error("This server doesn't have installed WorldEdit plugins.");
            return;
        }
        if(plugin.hasFAWE() && faweController != null) {
            faweController.pasteSchematic(Schematic, location);
            return;
        }
        worldEditController.pasteSchematic(Schematic,location);
    }
    public FAWEController getFAWE() {
        return faweController;
    }
    public WorldEditController getWE() {
        return worldEditController;
    }
}
