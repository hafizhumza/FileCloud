
package com.filecloud.documentservice.security.util;

import com.filecloud.documentservice.configuration.ApplicationContextUtil;
import com.filecloud.documentservice.exception.InvalidAccessException;
import com.filecloud.documentservice.security.dto.UserSession;
import com.filecloud.documentservice.util.Util;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;


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

    /**
     * Gets current logged in user id and email based on access token.
     *
     * @return current logged in user
     */
    public static UserSession getCurrentLoggedInUser() {
        OAuth2AuthenticationDetails authenticationDetails = getAuthenticationDetails();
        UserSession userSession = (UserSession) authenticationDetails.getDecodedDetails();

        if (userSession == null)
            throw new InvalidAccessException();

        return userSession;
    }
}
