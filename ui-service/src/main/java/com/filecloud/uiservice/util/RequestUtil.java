package com.filecloud.uiservice.util;

import com.filecloud.uiservice.constant.ConstUtil;
import com.filecloud.uiservice.dto.mvcmodel.LoginModel;

import java.util.HashMap;
import java.util.Map;

public class RequestUtil {

    public static Map<String, ?> getLoginRequest(LoginModel model) {
        Map<String, String> map = new HashMap<>();
        map.put("username", model.getEmail());
        map.put("password", model.getPassword());
        map.put("grant_type", "password");
        map.put("scope", ConstUtil.SCOPE_READ + " " + ConstUtil.SCOPE_WRITE + " " + ConstUtil.SCOPE_DOCUMENT + " " + ConstUtil.SCOPE_EMAIL);
        return map;
    }
}
