
package com.filecloud.documentservice.properties;

public class SecurityProperties {

    private boolean enabled;

    private String encryptionKey;

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

    public String[] getIgnoredPaths() {
        return ignoredPaths;
    }

    public void setIgnoredPaths(String[] ignoredPaths) {
        this.ignoredPaths = ignoredPaths;
    }
}
