package dev.mruniverse.guardianlib.core.schematics;

import com.boydti.fawe.FaweAPI;
import com.boydti.fawe.object.schematic.Schematic;
import dev.mruniverse.guardianlib.core.GuardianLIB;
import dev.mruniverse.guardianlib.core.utils.Logger;
import org.bukkit.Location;

import java.io.File;
import com.sk89q.worldedit.Vector;

@SuppressWarnings("unused")
public class FAWEController {
    private final Logger logs = GuardianLIB.getControl().getLogs();
    public void pasteSchematic(File schematic, Location location) {
        try {
            Vector vector = new Vector(location.getX(), location.getY(), location.getZ());
            Schematic schemToPaste = getSchematic(schematic);
            if(location.getWorld() == null) return;
            if (schemToPaste != null) {
                schemToPaste.paste(FaweAPI.getWorld(location.getWorld().getName()), vector);
            }
        }catch (Throwable throwable) {
            logs.error("Can't paste schematic using FAWE Controller, Throwable: ");
            logs.error(throwable);
        }
    }
    public Schematic getSchematic(File schematicFile) {
        try {
            return FaweAPI.load(schematicFile);
        } catch (Throwable throwable) {
            logs.error("Can't get schematic using FAWE Controller, Throwable: ");
            logs.error(throwable);
        }
        return null;
    }
}
