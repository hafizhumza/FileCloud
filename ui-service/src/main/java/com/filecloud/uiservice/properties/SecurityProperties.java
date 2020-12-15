
package com.filecloud.uiservice.properties;

public class SecurityProperties {

    private boolean enabled;

    private String encryptionKey;

    private String clientId;

    private String clientSecret;

    private String[] ignoredPaths;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getEncryptionKey() {
        return encryptionKey;
    }

    public void setEncryptionKey(String encryptionKey) {
        this.encryptionKey = encryptionKey;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String[] getIgnoredPaths() {
        return ignoredPaths;
    }

    public void setIgnoredPaths(String[] ignoredPaths) {
        this.ignoredPaths = ignoredPaths;
    }
}
