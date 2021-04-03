package dev.mruniverse.guardianlib.core.schematics;

import dev.mruniverse.guardianlib.core.GuardianLIB;
import dev.mruniverse.guardianlib.core.utils.Logger;
import org.bukkit.Location;

import java.io.File;

public class WEController implements WorldEditController {
    private final Logger logs = GuardianLIB.getControl().getLogs();
    public void pasteSchematic(File schematic, Location location) {
        logs.info("Schematic system is disabled for now, please install FastAsyncWorldEdit to use the another schematic system of the plugin, to fix temporal error");
        logs.info("This is not your error, this version of the plugin don't have WorldEdit-Without-FastAsyncWorldEdit Support");
        logs.info("Reason: Maven Issues, Class Issue: 'com.sk89q.worldedit.world.registry.WorldData' not found");
        logs.info("This issue will be fixed in the future.");
        //World world = new BukkitWorld(location.getWorld());
        //try {
            //Vector vector = new Vector(location.getX(),location.getY(),location.getZ());
            //ClipboardFormat.SCHEMATIC.load(schematic).paste(world, vector, false, true, null);

        //} catch (Throwable throwable) {
        //    logs.error("Can't paste schematic :(");
        //}
    }
}
