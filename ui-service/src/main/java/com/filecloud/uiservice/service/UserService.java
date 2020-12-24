package com.filecloud.uiservice.service;

import com.filecloud.uiservice.client.endpoint.AuthServerClient;
import com.filecloud.uiservice.client.request.ChangeForgotPasswordRequest;
import com.filecloud.uiservice.client.request.ChangePasswordRequest;
import com.filecloud.uiservice.client.request.ForgotPasswordRequest;
import com.filecloud.uiservice.client.request.RegisterUserRequest;
import com.filecloud.uiservice.client.response.ForgotPasswordVerifiedResponse;
import com.filecloud.uiservice.client.response.LoginResponse;
import com.filecloud.uiservice.client.response.UserDto;
import com.filecloud.uiservice.dto.mvcmodel.LoginModel;
import com.filecloud.uiservice.dto.session.UserSession;
import com.filecloud.uiservice.response.Response.Status;
import com.filecloud.uiservice.response.Result;
import com.filecloud.uiservice.security.util.AuthUtil;
import com.filecloud.uiservice.util.RequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService extends BaseService {

    private final AuthServerClient authServerClient;

    @Autowired
    public UserService(AuthServerClient authServerClient) {
        this.authServerClient = authServerClient;
    }

    public UserSession login(LoginModel model) {
        LoginResponse response = authServerClient.login(AuthUtil.getClientBasicAuthHeader(), RequestUtil.getLoginRequest(model));
        Result<UserDto> result = authServerClient.userinfo(AuthUtil.getBearerToken(response.getAccess_token()));
        logIfError(result);

        UserSession session = new UserSession();
        session.setData(response, result.getData());
        return session;
    }

    public void register(RegisterUserRequest request) {
        // TODO
    }

    public String forgotPassword(ForgotPasswordRequest request) {
        Result<String> result = authServerClient.forgotPassword(request);

        if (result.getStatusCode() == Status.RECORD_NOT_FOUND.getStatusCode())
            return "No account found with given email address";

        throwIfError(result);
        return result.getMessage();
    }

    public ForgotPasswordVerifiedResponse verifyForgotPasswordToken(String token) {
        Result<ForgotPasswordVerifiedResponse> result = authServerClient.verifyForgotPasswordToken(token);
        throwIfError(result);
        return result.getData();
    }

    public String changeForgotPassword(ChangeForgotPasswordRequest request) {
        Result<String> result = authServerClient.changeForgotPassword(request);

        if (result.getStatusCode() == Status.INVALID_INPUT.getStatusCode())
            return "Passwords not match";

        throwIfError(result);
        return result.getMessage();
    }

    public void changePassword(String token, ChangePasswordRequest request) {
        // TODO
    }

    public void listActiveUsers(String token) {
        // TODO
    }

}
