package dev.mruniverse.guardianlib.core.schematics;

import dev.mruniverse.guardianlib.core.GuardianLIB;
import org.bukkit.Location;

import java.io.File;
@SuppressWarnings("unused")
public class SchematicManager {
    private final GuardianLIB plugin;
    private final FAWEController faweController;
    private final WEController weController;
    public SchematicManager(GuardianLIB plugin) {
        this.plugin = plugin;
        faweController = new FAWEController();
        weController = new WEController();
    }
    public void pasteSchematic(File Schematic, Location location) {
        if(plugin.hasFAWE()) {
            faweController.pasteSchematic(Schematic,location);
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
