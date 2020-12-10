
package com.filecloud.adminservice.properties;

public class SecurityProperties {

	private boolean enabled;

	private String[] ignoredPaths;

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
