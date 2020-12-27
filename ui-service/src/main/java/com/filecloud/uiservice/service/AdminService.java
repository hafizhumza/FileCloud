package com.filecloud.uiservice.service;

import com.filecloud.uiservice.client.endpoint.AdminServiceClient;
import com.filecloud.uiservice.client.endpoint.AuthServerClient;
import com.filecloud.uiservice.client.request.IdRequest;
import com.filecloud.uiservice.client.response.SingleFieldResponse;
import com.filecloud.uiservice.client.response.UserResponse;
import com.filecloud.uiservice.response.Result;
import com.filecloud.uiservice.security.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService extends BaseService {

    private final AuthServerClient authServerClient;

    private final AdminServiceClient adminServiceClient;

    private final UserService userService;

    @Autowired
    public AdminService(AdminServiceClient adminServiceClient, AuthServerClient authServerClient, UserService userService) {
        this.adminServiceClient = adminServiceClient;
        this.authServerClient = authServerClient;
        this.userService = userService;
    }

    public List<UserResponse> listUsers(String token, String mode) {
        Result<List<UserResponse>> result;
        String bearerToken = AuthUtil.getBearerToken(token);

        if (mode.equalsIgnoreCase("active"))
            result = adminServiceClient.activeUsers(bearerToken);
        else if (mode.equalsIgnoreCase("locked"))
            result = adminServiceClient.inactiveusers(bearerToken);
        else if (mode.equalsIgnoreCase("admin"))
            result = adminServiceClient.listRoleAdmin(bearerToken);
        else if (mode.equalsIgnoreCase("user"))
            result = adminServiceClient.listRoleUser(bearerToken);
        else
            result = adminServiceClient.listUsers(bearerToken);

        throwIfInvalidAccess(result);
        return result.getData();
    }

    public String enableUser(String token, long userId) {
        Result<String> result = adminServiceClient.enableUser(AuthUtil.getBearerToken(token), new IdRequest(userId));
        logIfError(result);
        return result.getMessage();
    }

    public String disableUser(String token, long userId) {
        Result<String> result = adminServiceClient.disableUser(AuthUtil.getBearerToken(token), new IdRequest(userId));
        logIfError(result);
        return result.getMessage();
    }

    public String deleteUser(String token, long userId) {
        Result<String> result = adminServiceClient.deleteUser(AuthUtil.getBearerToken(token), new IdRequest(userId));
        logIfError(result);
        return result.getMessage();
    }

    public UserResponse getUser(String token, long userId) {
        String bearerToken = AuthUtil.getBearerToken(token);
        Result<UserResponse> result = authServerClient.getUser(bearerToken, userId);
        logIfError(result);
        return result.getData();
    }

    public String activeUserCount(String bearerToken) {
        return userService.activeUserCount(bearerToken);
    }

    public String inActiveUserCount(String bearerToken) {
        Result<SingleFieldResponse> result = adminServiceClient.inActiveUserCount(bearerToken);
        logIfError(result);
        return result.getData().getResponse().toString();
    }

    public String allUsersCount(String bearerToken) {
        Result<SingleFieldResponse> result = adminServiceClient.allUsersCount(bearerToken);
        logIfError(result);
        return result.getData().getResponse().toString();
    }

}
