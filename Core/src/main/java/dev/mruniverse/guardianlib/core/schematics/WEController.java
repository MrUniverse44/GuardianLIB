package dev.mruniverse.guardianlib.core.schematics;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.world.World;
import dev.mruniverse.guardianlib.core.GuardianLIB;
import dev.mruniverse.guardianlib.core.utils.Logger;
import org.bukkit.Location;

import java.io.File;

public class WEController implements WorldEditController {
    private final Logger logs = GuardianLIB.getControl().getLogs();
    public void pasteSchematic(File schematic, Location location) {
        World world = new BukkitWorld(location.getWorld());
        try {
            Vector vector = new Vector(location.getX(),location.getY(),location.getZ());
            ClipboardFormat.SCHEMATIC.load(schematic).paste(world, vector, false, true, null);
        } catch (Throwable throwable) {
            logs.error("Can't paste schematic :(");
        }
    }
}
