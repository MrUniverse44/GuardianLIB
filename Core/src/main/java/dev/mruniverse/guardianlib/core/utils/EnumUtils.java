package dev.mruniverse.guardianlib.core.utils;

public class EnumUtils {
    public static <E extends Enum<E>> boolean isValidEnum(Class<E> paramClass,String paramString) {
        if (paramString == null) return false;
        try {
            Enum.valueOf(paramClass,paramString);
            return true;
        }catch (Throwable ignored) { }
        return false;
    }
}
