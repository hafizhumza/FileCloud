package com.filecloud.uiservice.service;

import com.filecloud.uiservice.client.endpoint.AuthServerClient;
import com.filecloud.uiservice.dto.mvcmodel.LoginModel;
import com.filecloud.uiservice.client.response.LoginResponse;
import com.filecloud.uiservice.client.response.ResponseUserDto;
import com.filecloud.uiservice.properties.UiServiceProperties;
import com.filecloud.uiservice.response.Result;
import com.filecloud.uiservice.security.util.AuthUtil;
import com.filecloud.uiservice.util.RequestUtil;
import feign.FeignException.BadRequest;
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

    public boolean login(LoginModel model) {

        try {
            LoginResponse response = authServerClient.login(AuthUtil.getClientBasicAuthHeader(), RequestUtil.getLoginRequest(model));
            Result<ResponseUserDto> userinfo = authServerClient.userinfo(AuthUtil.getBearerToken(response.getAccess_token()));
            System.out.println(userinfo);
        } catch (BadRequest e) {
            return false;
        }

        return true;
    }

}
