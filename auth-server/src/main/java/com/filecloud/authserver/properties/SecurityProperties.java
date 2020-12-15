
package com.filecloud.authserver.properties;

public class SecurityProperties {

	private String[] ignoredPaths;

	private String passwordEncodingAlgorithm;

	public String getPasswordEncodingAlgorithm() {
		return passwordEncodingAlgorithm;
	}

	public void setPasswordEncodingAlgorithm(String passwordEncodingAlgorithm) {
		this.passwordEncodingAlgorithm = passwordEncodingAlgorithm;
	}

	public String[] getIgnoredPaths() {
		return ignoredPaths;
	}

	public void setIgnoredPaths(String[] ignoredPaths) {
		this.ignoredPaths = ignoredPaths;
	}
}
