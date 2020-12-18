package com.filecloud.uiservice.service;

import com.filecloud.uiservice.client.endpoint.GatewayServerClient;
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

    private final GatewayServerClient gatewayServerClient;

    @Autowired
    public UserService(UiServiceProperties uiServiceProperties, GatewayServerClient gatewayServerClient) {
        this.UiServiceProperties = uiServiceProperties;
        this.gatewayServerClient = gatewayServerClient;
    }

    public UserSession login(LoginModel model) {
        LoginDto response = gatewayServerClient.login(AuthUtil.getClientBasicAuthHeader(), RequestUtil.getLoginRequest(model));
        Result<UserDto> result = gatewayServerClient.userinfo(AuthUtil.getBearerToken(response.getAccess_token()));
        checkResult(result);

        UserSession session = new UserSession();

        // Remove Role_ prefix
        UserDto userDto = result.getData();
        userDto.setUserRole(Util.removeRolePrefix(userDto.getUserRole()));

        session.setData(response, userDto);
        return session;
    }

}
