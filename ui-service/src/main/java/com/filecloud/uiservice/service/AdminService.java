package com.filecloud.uiservice.service;

import com.filecloud.uiservice.client.endpoint.AdminServiceClient;
import com.filecloud.uiservice.client.response.SingleFieldDto;
import com.filecloud.uiservice.client.response.UserDto;
import com.filecloud.uiservice.properties.UiServiceProperties;
import com.filecloud.uiservice.response.Result;
import com.filecloud.uiservice.security.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService extends BaseService {

    private final UiServiceProperties uiServiceProperties;

    private final AdminServiceClient adminServiceClient;

    @Autowired
    public AdminService(UiServiceProperties uiServiceProperties, AdminServiceClient adminServiceClient) {
        this.uiServiceProperties = uiServiceProperties;
        this.adminServiceClient = adminServiceClient;
    }

    public List<UserDto> listUsers(String token, String mode) {
        Result<List<UserDto>> result;
        String bearerToken = AuthUtil.getBearerToken(token);

        if (mode.equalsIgnoreCase("active")) {
            result = adminServiceClient.activeUsers(bearerToken);
        } else if (mode.equalsIgnoreCase("locked")) {
            result = adminServiceClient.inactiveusers(bearerToken);
        } else {
            result = adminServiceClient.listUsers(bearerToken);
        }

        checkResult(result);
        return result.getData();
    }

    public void enableUser(long userId) {

    }

    public void disableUser(long userId) {

    }

    public void deleteUser(long userId) {

    }

    public void getUser(long userId) {

    }

    public String activeUserCount(String bearerToken) {
        Result<SingleFieldDto> result = adminServiceClient.activeUserCount(bearerToken);
        checkResult(result);
        return result.getData().getResponse().toString();
    }

    public String inActiveUserCount(String bearerToken) {
        Result<SingleFieldDto> result = adminServiceClient.inActiveUserCount(bearerToken);
        checkResult(result);
        return result.getData().getResponse().toString();
    }

    public String allUsersCount(String bearerToken) {
        Result<SingleFieldDto> result = adminServiceClient.allUsersCount(bearerToken);
        checkResult(result);
        return result.getData().getResponse().toString();
    }

}
