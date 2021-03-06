
package com.filecloud.uiservice.util;

import com.filecloud.uiservice.configuration.ConfigurationConstant;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Util {

    public static boolean isValidString(String data) {
        return !(data == null || data.trim().isEmpty());
    }

    public static boolean isValidArray(String[] array) {
        return (array != null && array.length > 0);
    }

    public static boolean isValidList(List<?> data) {
        return data != null && !data.isEmpty();
    }

    public static Boolean getConfigBoolean(String configVariable) {
        Boolean configBoolean = ConfigurationConstant.getProperty(configVariable, Boolean.class);
        return configBoolean != null && configBoolean;
    }

    public static String getConfigString(String configVariable) {
        String configString = ConfigurationConstant.getProperty(configVariable, String.class);
        return configString != null ? configString : "";
    }

    public static double convertBytesToMb(long bytes) {
        if (bytes <= 0)
            return 0;

        return bytes / 1024d / 1024d;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static double roundUptoTwo(double value) {
        return round(value, 2);
    }

    public static String humanReadableByteCountBin(long bytes) {
        long absB = bytes == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(bytes);

        if (absB < 1024)
            return bytes + " B";

        long value = absB;
        CharacterIterator ci = new StringCharacterIterator("KMGTPE");

        for (int i = 40; i >= 0 && absB > 0xfffccccccccccccL >> i; i -= 10) {
            value >>= 10;
            ci.next();
        }

        value *= Long.signum(bytes);
        return String.format("%.1f %cB", value / 1024.0, ci.current());
    }


    public static String getRandomUUID() {
        return UUID.randomUUID().toString();
    }

    public static long getDaysMillis(long days) {
        return TimeUnit.MILLISECONDS.convert(days, TimeUnit.DAYS);
    }

}
