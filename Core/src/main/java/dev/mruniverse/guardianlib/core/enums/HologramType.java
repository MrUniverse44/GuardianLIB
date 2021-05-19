package dev.mruniverse.guardianlib.core.enums;
@SuppressWarnings("unused")
public enum HologramType {
    GlobalHolograms,
    PacketHolograms,
    PersonalHolograms;

    public String getSignature() {
        switch (this){
            case GlobalHolograms:
                return "GH-";
            case PacketHolograms:
                return "PHL-";
            default:
            case PersonalHolograms:
                return "PH-";
        }
    }
}
