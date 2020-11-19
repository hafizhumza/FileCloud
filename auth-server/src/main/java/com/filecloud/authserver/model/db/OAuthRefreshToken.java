
package com.filecloud.authserver.model.db;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "oauth_refresh_token")
public class OAuthRefreshToken implements Serializable {

	@Id
	@Column(name = "token_id")
	String tokenId;

	@Column(name = "token", length = Integer.MAX_VALUE)
	byte[] token;

	@Column(name = "authentication", length = Integer.MAX_VALUE)
	byte[] authentication;

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

	public byte[] getAuthentication() {
		return authentication;
	}

	public void setAuthentication(byte[] authentication) {
		this.authentication = authentication;
	}
}
