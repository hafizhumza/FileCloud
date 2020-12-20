package com.filecloud.uiservice.service;

import com.filecloud.uiservice.client.endpoint.AuthServerClient;
import com.filecloud.uiservice.client.response.LoginDto;
import com.filecloud.uiservice.client.response.UserDto;
import com.filecloud.uiservice.dto.mvcmodel.LoginModel;
import com.filecloud.uiservice.dto.session.UserSession;
import com.filecloud.uiservice.properties.UiServiceProperties;
import com.filecloud.uiservice.response.Result;
import com.filecloud.uiservice.security.util.AuthUtil;
import com.filecloud.uiservice.util.RequestUtil;
import com.filecloud.uiservice.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService extends BaseService {

    private final UiServiceProperties UiServiceProperties;

    private final AuthServerClient authServerClient;

    @Autowired
    public UserService(UiServiceProperties uiServiceProperties, AuthServerClient authServerClient) {
        this.UiServiceProperties = uiServiceProperties;
        this.authServerClient = authServerClient;
    }

    public UserSession login(LoginModel model) {
        LoginDto response = authServerClient.login(AuthUtil.getClientBasicAuthHeader(), RequestUtil.getLoginRequest(model));
        Result<UserDto> result = authServerClient.userinfo(AuthUtil.getBearerToken(response.getAccess_token()));
        checkResult(result);

        UserSession session = new UserSession();
        session.setData(response, result.getData());
        return session;
    }

}
