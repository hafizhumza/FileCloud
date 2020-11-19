
package com.filecloud.authserver.configuration;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextUtil implements ApplicationContextAware {

    private static ApplicationContext appContext;

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        appContext = ctx;
    }

    public static ApplicationContext getApplicationContext() {
        return appContext;
    }
}
