package com.filecloud.uiservice.service;

import com.filecloud.uiservice.client.endpoint.AuthServerClient;
import com.filecloud.uiservice.client.request.*;
import com.filecloud.uiservice.client.response.ForgotPasswordVerifiedResponse;
import com.filecloud.uiservice.client.response.LoginResponse;
import com.filecloud.uiservice.client.response.UserResponse;
import com.filecloud.uiservice.dto.mvcmodel.LoginModel;
import com.filecloud.uiservice.dto.session.UserSession;
import com.filecloud.uiservice.response.Result;
import com.filecloud.uiservice.security.util.AuthUtil;
import com.filecloud.uiservice.util.RequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SharedService extends BaseService {

    private final AuthServerClient authServerClient;

    @Autowired
    public SharedService(AuthServerClient authServerClient) {
        this.authServerClient = authServerClient;
    }

    public UserSession login(LoginModel model) {
        LoginResponse response = authServerClient.login(AuthUtil.getClientBasicAuthHeader(), RequestUtil.getLoginRequest(model));
        Result<UserResponse> result = authServerClient.userinfo(AuthUtil.getBearerToken(response.getAccess_token()));
        logIfError(result);

        UserSession session = new UserSession();
        session.setData(response, result.getData());
        return session;
    }

    public Result<String> register(RegisterUserRequest request) {
        Result<String> result = authServerClient.register(request);
        throwIfInternalServerError(result);
        return result;
    }

    public Result<String> updateProfile(String token, UpdateUserRequest request) {
        Result<String> result = authServerClient.updateProfile(AuthUtil.getBearerToken(token), request);
        throwIfInvalidAccess(result);
        return result;
    }

    public Result<String> forgotPassword(ForgotPasswordRequest request) {
        Result<String> result = authServerClient.forgotPassword(request);
        throwIfInvalidAccess(result);
        return result;
    }

    public Result<ForgotPasswordVerifiedResponse> verifyForgotPasswordToken(String token) {
        Result<ForgotPasswordVerifiedResponse> result = authServerClient.verifyForgotPasswordToken(token);
        throwIfInvalidAccess(result);
        return result;
    }

    public Result<String> changeForgotPassword(ChangeForgotPasswordRequest request) {
        Result<String> result = authServerClient.changeForgotPassword(request);
        throwIfInvalidAccess(result);
        return result;
    }

    public Result<String> changePassword(String token, ChangePasswordRequest request) {
        Result<String> result = authServerClient.changePassword(AuthUtil.getBearerToken(token), request);
        throwIfInvalidAccess(result);
        return result;
    }

}
