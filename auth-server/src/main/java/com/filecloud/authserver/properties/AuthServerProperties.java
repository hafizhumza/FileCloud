package com.filecloud.authserver.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix = "auth-server")
public class AuthServerProperties {

	SecurityProperties security = new SecurityProperties();

	boolean devMode;

	String uiServiceScheme;

	String uiServiceHost;

	String uiServicePort;

	String uiServiceForgotPasswordUrl;

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

	public String getUiServiceScheme() {
		return uiServiceScheme;
	}

	public void setUiServiceScheme(String uiServiceScheme) {
		this.uiServiceScheme = uiServiceScheme;
	}

	public String getUiServiceHost() {
		return uiServiceHost;
	}

	public void setUiServiceHost(String uiServiceHost) {
		this.uiServiceHost = uiServiceHost;
	}

	public String getUiServicePort() {
		return uiServicePort;
	}

	public void setUiServicePort(String uiServicePort) {
		this.uiServicePort = uiServicePort;
	}

	public String getUiServiceForgotPasswordUrl() {
		return uiServiceForgotPasswordUrl;
	}

	public void setUiServiceForgotPasswordUrl(String uiServiceForgotPasswordUrl) {
		this.uiServiceForgotPasswordUrl = uiServiceForgotPasswordUrl;
	}
}
