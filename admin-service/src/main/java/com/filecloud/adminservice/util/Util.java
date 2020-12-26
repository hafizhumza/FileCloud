
package com.filecloud.adminservice.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.filecloud.adminservice.configuration.ConfigurationConstant;

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

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static double roundUptoTwo(double value) {
        return round(value, 2);
    }

    public static String getRandomUUID() {
        return UUID.randomUUID().toString();
    }

    public static long getDaysMillis(long days) {
        return TimeUnit.MILLISECONDS.convert(days, TimeUnit.DAYS);
    }

}
