
package com.filecloud.authserver.security.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;

import com.filecloud.authserver.configuration.ApplicationContextUtil;
import com.filecloud.authserver.exception.InvalidAccessException;
import com.filecloud.authserver.util.Util;


public class AuthUtil {

	public static boolean revokeCurrentToken() {
		return revokeToken(((OAuth2AuthenticationDetails) getAuthenticationDetails()).getTokenValue());
	}

	public static boolean revokeToken(String token) {
		try {
			ConsumerTokenServices tokenServices = (ConsumerTokenServices) ApplicationContextUtil.getApplicationContext().getBean("tokenServices");
			return (Util.isValidString(token) && tokenServices.revokeToken(token));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getPrincipal() {
		return (T) ((Authentication) getAuthentication()).getPrincipal();
	}

	@SuppressWarnings("unchecked")
	public static <T> T getAuthentication() {
		return (T) SecurityContextHolder.getContext().getAuthentication();
	}

	@SuppressWarnings("unchecked")
	public static <T> T getAuthenticationDetails() {
		return (T) ((Authentication) getAuthentication()).getDetails();
	}

	public static String getBearerToken() {
		OAuth2AuthenticationDetails authenticationDetails = getAuthenticationDetails();

		if (authenticationDetails == null)
			throw new InvalidAccessException();

		return authenticationDetails.getTokenType().concat(" ").concat(authenticationDetails.getTokenValue());
	}
}
