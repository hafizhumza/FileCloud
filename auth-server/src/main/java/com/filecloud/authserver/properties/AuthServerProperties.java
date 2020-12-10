package com.filecloud.authserver.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix = "auth-server")
public class AuthServerProperties {

	SecurityProperties security = new SecurityProperties();

	boolean devMode;

	public SecurityProperties security() {
		return security;
	}

	public void setSecurity(SecurityProperties security) {
		this.security = security;
	}

	public SecurityProperties getSecurity() {
		return security;
	}

	public boolean isDevMode() {
		return devMode;
	}

	public void setDevMode(boolean devMode) {
		this.devMode = devMode;
	}
}
