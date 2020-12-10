package com.filecloud.adminservice.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix = "admin-service", ignoreUnknownFields = false)
public class AdminServiceProperties {

	SecurityProperties security = new SecurityProperties();

	private boolean isDevMode;

	public boolean isDevMode() {
		return isDevMode;
	}

	public void setDevMode(boolean isDevMode) {
		this.isDevMode = isDevMode;
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
