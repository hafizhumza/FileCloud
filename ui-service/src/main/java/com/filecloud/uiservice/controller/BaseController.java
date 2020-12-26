
package com.filecloud.uiservice.controller;

import com.filecloud.uiservice.constant.UiConst;
import com.filecloud.uiservice.dto.session.UserSession;
import com.filecloud.uiservice.security.util.AuthUtil;
import com.filecloud.uiservice.service.BaseService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class BaseController {

    public static String getBearerToken(UserSession session) {
        if (session == null)
            BaseService.sessionExpired();

        return AuthUtil.getBearerToken(session.getAccessToken());
    }

    public static UserSession getCurrentUser(HttpSession session) {
        if (session == null)
            BaseService.sessionExpired();

        UserSession userSession = (UserSession) session.getAttribute(UiConst.SESSION_CURRENT_USER);

        if (userSession == null)
            BaseService.sessionExpired();

        return userSession;
    }

    public static UserSession getVerifiedAdmin(HttpSession session) {
        UserSession user = getCurrentUser(session);

        if (!isAdmin(user))
            BaseService.invalidAccess();

        return user;
    }

    public static boolean isAdmin(UserSession session) {
        if (session == null)
            BaseService.sessionExpired();

        return session.getRole().equals(UiConst.ROLE_ADMIN);
    }

    public static UserSession getVerifiedUser(HttpSession session) {
        UserSession user = getCurrentUser(session);

        if (isAdmin(user))
            BaseService.invalidAccess();

        return user;
    }

    public static void persistSession(HttpServletRequest request, UserSession userSession) {
        if (request == null)
            BaseService.invalidInput();

        if (userSession == null)
            BaseService.sessionExpired();

        request.getSession().setAttribute(UiConst.SESSION_CURRENT_USER, userSession);
    }

    public static void invalidateSession(HttpServletRequest request) {
        if (request == null)
            BaseService.invalidInput();

        request.getSession().invalidate();
    }

}
