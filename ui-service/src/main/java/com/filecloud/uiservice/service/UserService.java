package com.filecloud.uiservice.service;

import com.filecloud.uiservice.client.AuthServerClient;
import com.filecloud.uiservice.dto.mvcmodel.LoginModel;
import com.filecloud.uiservice.dto.response.LoginResponse;
import com.filecloud.uiservice.properties.UiServiceProperties;
import com.filecloud.uiservice.security.util.AuthUtil;
import com.filecloud.uiservice.util.RequestUtil;
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

    public void login(LoginModel model) {
        LoginResponse response = authServerClient.login(AuthUtil.getClientBasicAuthHeader(), RequestUtil.getLoginRequest(model));
        System.out.println(response);
    }

}
