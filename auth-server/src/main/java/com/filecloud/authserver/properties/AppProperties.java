package com.filecloud.authserver.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    SecurityProperties security = new SecurityProperties();

    public SecurityProperties security() {
        return security;
    }

    public void setSecurity(SecurityProperties security) {
        this.security = security;
    }

    public SecurityProperties getSecurity() {
        return security;
    }
}
