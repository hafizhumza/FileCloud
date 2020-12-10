
package com.filecloud.adminservice.security.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;

import com.filecloud.adminservice.configuration.ApplicationContextUtil;
import com.filecloud.adminservice.exception.InvalidAccessException;
import com.filecloud.adminservice.util.Util;


public class AuthUtil {

	private static Boolean securityEnabled = null;

	public static boolean isSecurityEnabled() {

		if (securityEnabled == null)
			securityEnabled = Util.getConfigBoolean("app.security.enabled");

		return securityEnabled;
	}

	public static boolean revokeCurrentToken() {
		return revokeToken(((OAuth2AuthenticationDetails) AuthUtil.getAuthenticationDetails()).getTokenValue());
	}

	public static boolean revokeToken(String token) {

		if (!isSecurityEnabled())
			return true;

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
