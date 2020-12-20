package com.filecloud.uiservice.util;

import com.filecloud.uiservice.constant.UiConst;
import com.filecloud.uiservice.dto.mvcmodel.LoginModel;

import java.util.HashMap;
import java.util.Map;

public class RequestUtil {

    public static Map<String, ?> getLoginRequest(LoginModel model) {
        Map<String, String> map = new HashMap<>();
        map.put("username", model.getEmail());
        map.put("password", model.getPassword());
        map.put("grant_type", "password");
        map.put("scope", UiConst.SCOPE_READ + " " + UiConst.SCOPE_WRITE + " " + UiConst.SCOPE_DOCUMENT + " " + UiConst.SCOPE_EMAIL);
        return map;
    }
}
