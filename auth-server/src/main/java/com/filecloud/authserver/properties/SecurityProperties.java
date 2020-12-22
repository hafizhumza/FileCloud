
package com.filecloud.authserver.properties;

public class SecurityProperties {

	private String[] ignoredPaths;

	private int forgotPasswordLinkExpiryDays;

	public String[] getIgnoredPaths() {
		return ignoredPaths;
	}

	public void setIgnoredPaths(String[] ignoredPaths) {
		this.ignoredPaths = ignoredPaths;
	}

	public int getForgotPasswordLinkExpiryDays() {
		return forgotPasswordLinkExpiryDays;
	}

	public void setForgotPasswordLinkExpiryDays(int forgotPasswordLinkExpiryDays) {
		this.forgotPasswordLinkExpiryDays = forgotPasswordLinkExpiryDays;
	}

}
