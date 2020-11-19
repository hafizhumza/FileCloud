
package com.filecloud.authserver.util;

import com.filecloud.authserver.configuration.ConfigurationConstant;

import java.util.List;

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
}
