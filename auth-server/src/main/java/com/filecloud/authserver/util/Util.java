
package com.filecloud.authserver.util;

import java.util.List;

import com.filecloud.authserver.configuration.ConfigurationConstant;


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

	public static String removeRolePrefix(String role) {
		if (isValidString(role))
			return role.substring(role.indexOf("_") + 1).toUpperCase(); // Remove ROLE_ prefix

		return null;
	}
}
