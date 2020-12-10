
package com.filecloud.adminservice.configuration;

import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

public class ConfigurationConstant {

    public static Environment getEnvironment() {
        return ApplicationContextUtil.getApplicationContext().getEnvironment();
    }

    public static <T> T getProperty(String propertyName, Class<T> classType) {
        return getEnvironment().getProperty(propertyName, classType);
    }

    public static ApplicationContext getContext() {
        return ApplicationContextUtil.getApplicationContext();
    }
}
