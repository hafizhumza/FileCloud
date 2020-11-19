
package com.filecloud.authserver.properties;

public class SecurityProperties {

	private boolean enabled;

	private String[] ignoredPaths;

	private String passwordEncodingAlgorithm;

	public String getPasswordEncodingAlgorithm() {
		return passwordEncodingAlgorithm;
	}

	public void setPasswordEncodingAlgorithm(String passwordEncodingAlgorithm) {
		this.passwordEncodingAlgorithm = passwordEncodingAlgorithm;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String[] getIgnoredPaths() {
		return ignoredPaths;
	}

	public void setIgnoredPaths(String[] ignoredPaths) {
		this.ignoredPaths = ignoredPaths;
	}
}
