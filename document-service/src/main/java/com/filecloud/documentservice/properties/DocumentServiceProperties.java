package com.filecloud.documentservice.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix = "document-service", ignoreUnknownFields = false)
public class DocumentServiceProperties {

	SecurityProperties security = new SecurityProperties();

	String uploadedDocumentsDir;

	/**
	 * This will be in MB
	 */
	double spaceLimitPerUser;

	boolean devMode;

	String gatewayServerScheme;

	String gatewayServerHost;

	String gatewayServerPort;

	String documentServicePrefix;

	/**
	 * This will be in days
	 */
	long sharedDocumentsExpiryDays;

	public String getUploadedDocumentsDir() {
		return uploadedDocumentsDir;
	}

	public void setUploadedDocumentsDir(String uploadedDocumentsDir) {
		this.uploadedDocumentsDir = uploadedDocumentsDir;
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

	public String getDocumentServicePrefix() {
		return documentServicePrefix;
	}

	public void setDocumentServicePrefix(String documentServicePrefix) {
		this.documentServicePrefix = documentServicePrefix;
	}

	public double getSpaceLimitPerUser() {
		return spaceLimitPerUser;
	}

	public void setSpaceLimitPerUser(double spaceLimitPerUser) {
		this.spaceLimitPerUser = spaceLimitPerUser;
	}

	public boolean isDevMode() {
		return devMode;
	}

	public void setDevMode(boolean devMode) {
		this.devMode = devMode;
	}

	public long getSharedDocumentsExpiryDays() {
		return sharedDocumentsExpiryDays;
	}

	public void setSharedDocumentsExpiryDays(long sharedDocumentsExpiryDays) {
		this.sharedDocumentsExpiryDays = sharedDocumentsExpiryDays;
	}

	public String getGatewayServerScheme() {
		return gatewayServerScheme;
	}

	public void setGatewayServerScheme(String gatewayServerScheme) {
		this.gatewayServerScheme = gatewayServerScheme;
	}

	public String getGatewayServerHost() {
		return gatewayServerHost;
	}

	public void setGatewayServerHost(String gatewayServerHost) {
		this.gatewayServerHost = gatewayServerHost;
	}

	public String getGatewayServerPort() {
		return gatewayServerPort;
	}

	public void setGatewayServerPort(String gatewayServerPort) {
		this.gatewayServerPort = gatewayServerPort;
	}

}
