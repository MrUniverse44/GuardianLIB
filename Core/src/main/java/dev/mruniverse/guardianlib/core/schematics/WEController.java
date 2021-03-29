package dev.mruniverse.guardianlib.core.schematics;

import com.boydti.fawe.FaweAPI;
import com.boydti.fawe.object.schematic.Schematic;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.function.mask.ExistingBlockMask;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.registry.WorldData;
import dev.mruniverse.guardianlib.core.GuardianLIB;
import dev.mruniverse.guardianlib.core.utils.Logger;
import org.bukkit.Location;

import java.io.File;
import java.io.FileInputStream;

@SuppressWarnings("unused")
public class WEController {
    private final Logger logs = GuardianLIB.getControl().getLogs();
    public void pasteSchematic(File schematic, Location location) {
        Vector to = new Vector(location.getX(), location.getY(), location.getZ());
        World weWorld = new BukkitWorld(location.getWorld());
        WorldData worldData = weWorld.getWorldData();
        Clipboard clipboard;
        try {
            clipboard = ClipboardFormat.SCHEMATIC.getReader(new FileInputStream(schematic)).read(worldData);
            Extent source = clipboard;
            Extent destination = WorldEdit.getInstance().getEditSessionFactory().getEditSession(weWorld, -1);
            ForwardExtentCopy copy = new ForwardExtentCopy(source, clipboard.getRegion(), clipboard.getOrigin(), destination, to);
            copy.setSourceMask(new ExistingBlockMask(clipboard));
            Operations.completeLegacy(copy);
        } catch (Throwable throwable) {
            logs.error("Can't paste schematic using WE Controller, Throwable:");
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
