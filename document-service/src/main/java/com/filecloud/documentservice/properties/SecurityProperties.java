
package com.filecloud.documentservice.properties;

public class SecurityProperties {

	private String encryptionKey;

	private String[] ignoredPaths;

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
