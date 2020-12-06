package com.filecloud.emailservice.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "email-service", ignoreUnknownFields = false)
public class EmailServiceProperties {

    SecurityProperties security = new SecurityProperties();

    boolean devMode;

    public SecurityProperties security() {
        return security;
    }

    public SecurityProperties getSecurity() {
        return security;
    }

    public void setSecurity(SecurityProperties security) {
        this.security = security;
    }

    public boolean isDevMode() {
        return devMode;
    }

    public void setDevMode(boolean devMode) {
        this.devMode = devMode;
    }

}
