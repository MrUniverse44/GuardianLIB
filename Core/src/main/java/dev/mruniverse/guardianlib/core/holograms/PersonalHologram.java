package dev.mruniverse.guardianlib.core.holograms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dev.mruniverse.guardianlib.core.GuardianLIB;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class PersonalHologram {
    private final GuardianLIB guardianLIB;
    private final String holoPrivateID;
    private final Player player;
    private final List<String> holoAS;
    private Location holoLocation;

    private List<String> holoLines;

    public void setLocation(Location holoLocation) {
        this.holoLocation = holoLocation;
    }

    public void setLines(List<String> holoLines) {
        this.holoLines = holoLines;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    private double distance = 0.3D;

    public double getDistance() {
        return this.distance;
    }

    public PersonalHologram(GuardianLIB guardianLIB, Player player, Location location, String holoPrivateID, String[] lines) {
        this.guardianLIB = guardianLIB;
        this.holoLocation = location;
        this.holoPrivateID = holoPrivateID;
        this.player = player;
        this.holoLines = Arrays.asList(lines);
        this.holoAS = new ArrayList<>();
    }
    public PersonalHologram(GuardianLIB guardianLIB, Player player, Location location, String holoPrivateID, List<String> lines) {
        this.guardianLIB = guardianLIB;
        this.holoLocation = location;
        this.holoPrivateID = holoPrivateID;
        this.player = player;
        this.holoLines = lines;
        this.holoAS = new ArrayList<>();
    }
    public void spawn() {
        int lineID = 0;
        for (String line : this.holoLines) {
            Location holoLineLocation = this.holoLocation.clone();
            holoLineLocation.setY(this.holoLocation.getY() + this.distance * this.holoLines.size());
            if (lineID > 0) holoLineLocation = guardianLIB.getNMS().getHologramLocation(holoPrivateID + "-" + (lineID - 1));
            holoLineLocation.setY(holoLineLocation.getY() - this.distance);
            guardianLIB.getNMS().spawnHologram(this.player,holoPrivateID + "-" + lineID,line,holoLineLocation);
            holoAS.add(holoPrivateID + "-" + lineID);
            lineID++;
        }
    }

    public void update() {
        for (int i = 0; i < this.holoLines.size(); i++) {
            guardianLIB.getNMS().updateHologramText(this.player,holoPrivateID + "-" + i,this.holoLines.get(i));
        }
    }

    public void delete() {
        for (int i = 0; i < this.holoLines.size(); i++) {
            guardianLIB.getNMS().deleteHologram(this.player,holoPrivateID + "-" + i);
        }
    }

    public Player getPlayer() { return this.player; }

    public Location getLocation() {
        return this.holoLocation;
    }

    public List<String> getLines() {
        return this.holoLines;
    }

    public List<String> getArmorStands() {
        return this.holoAS;
    }

}
