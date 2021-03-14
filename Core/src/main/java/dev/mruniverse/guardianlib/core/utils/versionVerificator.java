package dev.mruniverse.guardianlib.core.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.Bukkit;

public class versionVerificator {
    public static String extractNMSVersion() {
        Matcher matcher = Pattern.compile("v\\d+_\\d+_R\\d+").matcher(Bukkit.getServer().getClass().getPackage().getName());
        if (matcher.find())
            return matcher.group();
        return null;
    }

    public static boolean isNewVersion() {
        try {
            String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
            switch (version) {
                case "v1_16_R3":
                case "v1_16_R2":
                case "v1_16_R1":
                case "v1_14_R1":
                case "v1_15_R1":
                case "v1_13_R2":
                case "v1_13_R1":
                    return true;
                default:
                    return false;
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
            return false;
        }
    }
}
