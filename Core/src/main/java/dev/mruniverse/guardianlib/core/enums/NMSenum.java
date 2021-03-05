package dev.mruniverse.guardianlib.core.enums;

import dev.mruniverse.guardianlib.core.utils.versionVerificator;

@SuppressWarnings("unused")
public enum NMSenum {
    v1_8_R1,v1_8_R2, v1_8_R3, v1_9_R1, v1_9_R2, v1_10_R1, v1_11_R1, v1_12_R1, v1_13_R1, v1_13_R2, v1_14_R1, v1_15_R1, v1_16_R1, v1_16_R2, v1_16_R3;

    private static final NMSenum CURRENT_VERSION;

    static {
        CURRENT_VERSION = extractCurrentVersion();
    }

    private static NMSenum extractCurrentVersion() {
        String nmsVersionName = versionVerificator.extractNMSVersion();
        if (nmsVersionName != null)
            try {
                return valueOf(nmsVersionName);
            } catch (IllegalArgumentException e) {
                return null;
            }
        return null;
    }

    public static boolean validVersion() {
        return (CURRENT_VERSION != null);
    }

    public static NMSenum getCurrent() {
        if (CURRENT_VERSION == null)
            throw new IllegalStateException("Current version not set");
        return CURRENT_VERSION;
    }

    public static boolean isGreaterEqualThan(NMSenum other) {
        return (getCurrent().ordinal() >= other.ordinal());
    }

    public static boolean isBetween(NMSenum from, NMSenum to) {
        return (from.ordinal() <= getCurrent().ordinal() && getCurrent().ordinal() <= to.ordinal());
    }
}

