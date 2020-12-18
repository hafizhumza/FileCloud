
package com.filecloud.uiservice.security.util;

import com.filecloud.uiservice.util.Util;
import org.springframework.util.Base64Utils;


public class AuthUtil {

    private static String clientId = null;

    private static String clientSecret = null;

    public static String getClientId() {
        if (clientId == null)
            clientId = Util.getConfigString("ui-service.security.client-id");

        return clientId;
    }

    public static String getClientSecret() {
        if (clientSecret == null)
            clientSecret = Util.getConfigString("ui-service.security.client-secret");

        return clientSecret;
    }

    public static String getClientBasicAuthHeader() {
        byte[] encodedBytes = Base64Utils.encode((getClientId() + ":" + getClientSecret()).getBytes());
        return "Basic " + new String(encodedBytes);
    }

    public static String getBearerToken(String token) {
        return "Bearer " + token;
    }
}
