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
}
