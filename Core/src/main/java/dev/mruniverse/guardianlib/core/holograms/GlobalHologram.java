package dev.mruniverse.guardianlib.core.holograms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;
import dev.mruniverse.guardianlib.core.GuardianLIB;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
@SuppressWarnings("unused")
public class GlobalHologram {
    private Location holoLocation;

    private final GuardianLIB guardianLIB = GuardianLIB.getControl();

    private List<String> holoLines;

    private List<ArmorStand> holoAS;

    private final int guardianPrivateID;

    private final String guardianID;

    public void setLocation(Location holoLocation) {
        this.holoLocation = holoLocation;
    }

    public void setLines(List<String> holoLines) {
        this.holoLines = holoLines;
    }

    public void setArmorStands(List<ArmorStand> holoAS) {
        this.holoAS = holoAS;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    private double distance = 0.3D;

    public double getDistance() {
        return this.distance;
    }

    public GlobalHologram(Location location, String[] lines) {
        this.holoLocation = location;
        this.holoLines = Arrays.asList(lines);
        this.holoAS = new ArrayList<>();
        guardianPrivateID = guardianLIB.getHologramsLoaded() + 1;
        guardianLIB.setHologramsLoaded(guardianLIB.getHologramsLoaded() + 1);
        guardianID = "GH-" + guardianPrivateID;
    }
    public GlobalHologram(Location location, List<String> lines) {
        this.holoLocation = location;
        this.holoLines = lines;
        this.holoAS = new ArrayList<>();
        guardianPrivateID = guardianLIB.getHologramsLoaded() + 1;
        guardianLIB.setHologramsLoaded(guardianLIB.getHologramsLoaded() + 1);
        guardianID = "GH-" + guardianPrivateID;
    }

    public void spawn() {
        int lineID = 0;
        for (String line : this.holoLines) {
            Location holoLineLocation = this.holoLocation.clone();
            holoLineLocation.setY(this.holoLocation.getY() + this.distance * this.holoLines.size());
            if (lineID > 0) holoLineLocation = this.holoAS.get(lineID - 1).getLocation();
            holoLineLocation.setY(holoLineLocation.getY() - this.distance);
            ArmorStand as = addArmorStand(line, holoLineLocation);
            this.holoAS.add(as);
            lineID++;
        }
        guardianLIB.armorStands.put(guardianPrivateID,holoAS);
    }
    public String getGuardianID() {
        return guardianID;
    }
    public void update() {
        for (int i = 0; i < this.holoLines.size(); i++) this.holoAS.get(i).setCustomName(this.holoLines.get(i));
        guardianLIB.armorStands.put(guardianPrivateID,holoAS);
    }

    public void remove() {
        int size = this.holoLines.size();
        List<ArmorStand> removeAS = Lists.newArrayList(this.holoAS);
        for(int i = 0; i < size; i++) {
            ArmorStand armorStand = removeAS.get(i);
            armorStand.remove();
        }
        guardianLIB.armorStands.put(guardianPrivateID, new ArrayList<>());
        this.holoAS = new ArrayList<>();
        this.holoLines = new ArrayList<>();
    }

    private ArmorStand addArmorStand(String line, Location lineLocation) {
        World holoWorld = this.holoLocation.getWorld();
        if(holoWorld == null) holoWorld = Bukkit.getWorlds().get(0);
        ArmorStand as = (ArmorStand)holoWorld.spawnEntity(lineLocation, EntityType.ARMOR_STAND);
        as.setCustomNameVisible(true);
        as.setSmall(true);
        as.setCustomName(line);
        as.setBasePlate(false);
        as.setGravity(false);
        as.setCanPickupItems(false);
        as.setVisible(false);
        as.setMarker(true);
        return as;
    }

    public Location getLocation() {
        return this.holoLocation;
    }

    public List<String> getLines() {
        return this.holoLines;
    }

    public List<ArmorStand> getArmorStands() {
        return this.holoAS;
    }

}

