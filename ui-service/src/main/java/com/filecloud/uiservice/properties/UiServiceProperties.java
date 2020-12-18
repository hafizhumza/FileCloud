package com.filecloud.uiservice.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix = "ui-service", ignoreUnknownFields = false)
public class UiServiceProperties {

    SecurityProperties security = new SecurityProperties();

    String tempDir;

    public String getTempDir() {
        return tempDir;
    }

    public void setTempDir(String tempDir) {
        this.tempDir = tempDir;
    }

    public SecurityProperties security() {
        return security;
    }

    public SecurityProperties getSecurity() {
        return security;
    }

    public void setSecurity(SecurityProperties security) {
        this.security = security;
    }

}
