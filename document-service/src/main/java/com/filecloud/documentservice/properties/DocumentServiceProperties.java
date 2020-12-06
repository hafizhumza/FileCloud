package com.filecloud.documentservice.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "document-service", ignoreUnknownFields = false)
public class DocumentServiceProperties {

    SecurityProperties security = new SecurityProperties();

    String uploadedDocumentsDir;

    /**
     * This will be in MB
     */
    double spaceLimitPerUser;

    boolean devMode;

    /**
     * This will be in days
     */
    long sharedDocumentsExpiryDays;

    public String getUploadedDocumentsDir() {
        return uploadedDocumentsDir;
    }

    public void setUploadedDocumentsDir(String uploadedDocumentsDir) {
        this.uploadedDocumentsDir = uploadedDocumentsDir;
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

    public double getSpaceLimitPerUser() {
        return spaceLimitPerUser;
    }

    public void setSpaceLimitPerUser(double spaceLimitPerUser) {
        this.spaceLimitPerUser = spaceLimitPerUser;
    }

    public boolean isDevMode() {
        return devMode;
    }

    public void setDevMode(boolean devMode) {
        this.devMode = devMode;
    }

    public long getSharedDocumentsExpiryDays() {
        return sharedDocumentsExpiryDays;
    }

    public void setSharedDocumentsExpiryDays(long sharedDocumentsExpiryDays) {
        this.sharedDocumentsExpiryDays = sharedDocumentsExpiryDays;
    }
}
