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
	long spaceLimitPerUser;

	boolean devMode;

	String uiServiceScheme;

	String uiServiceHost;

	String uiServicePort;

	String uiServiceDownloadSharedDocumentUrl;

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

	public long getSpaceLimitPerUser() {
		return spaceLimitPerUser;
	}

	public void setSpaceLimitPerUser(long spaceLimitPerUser) {
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

	public String getUiServiceDownloadSharedDocumentUrl() {
		return uiServiceDownloadSharedDocumentUrl;
	}

	public void setUiServiceDownloadSharedDocumentUrl(String uiServiceDownloadSharedDocumentUrl) {
		this.uiServiceDownloadSharedDocumentUrl = uiServiceDownloadSharedDocumentUrl;
	}

}
