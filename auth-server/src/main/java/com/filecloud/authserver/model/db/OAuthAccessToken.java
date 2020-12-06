
package com.filecloud.authserver.model.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "oauth_access_token")
public class OAuthAccessToken {

	@Id
	@Column(name = "authentication_id")
	String authenticationId;

	@Column(name = "token_id")
	String tokenId;

	@Column(name = "token", length = Integer.MAX_VALUE)
	byte[] token;

	@Column(name = "user_name")
	String userName;

	@Column(name = "client_id")
	String clientId;

	@Column(name = "authentication", length = Integer.MAX_VALUE)
	byte[] authentication;

	@Column(name = "refresh_token")
	String refreshToken;

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public byte[] getToken() {
		return token;
	}

	public void setToken(byte[] token) {
		this.token = token;
	}

	public String getAuthenticationId() {
		return authenticationId;
	}

	public void setAuthenticationId(String authenticationId) {
		this.authenticationId = authenticationId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public byte[] getAuthentication() {
		return authentication;
	}

	public void setAuthentication(byte[] authentication) {
		this.authentication = authentication;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
}
