package com.filecloud.uiservice.service;

import com.filecloud.uiservice.client.endpoint.GatewayServerClient;
import com.filecloud.uiservice.client.response.SingleFieldDto;
import com.filecloud.uiservice.properties.UiServiceProperties;
import com.filecloud.uiservice.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService extends BaseService {

    private final UiServiceProperties uiServiceProperties;

    private final GatewayServerClient gatewayServerClient;

    @Autowired
    public AdminService(UiServiceProperties uiServiceProperties, GatewayServerClient gatewayServerClient) {
        this.uiServiceProperties = uiServiceProperties;
        this.gatewayServerClient = gatewayServerClient;
    }

    public void listUsers() {

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
        Result<SingleFieldDto> result = gatewayServerClient.activeUserCount(bearerToken);
        checkResult(result);
        return result.getData().getResponse().toString();
    }

    public String inActiveUserCount(String bearerToken) {
        Result<SingleFieldDto> result = gatewayServerClient.inActiveUserCount(bearerToken);
        checkResult(result);
        return result.getData().getResponse().toString();
    }

    public String allUsersCount(String bearerToken) {
        Result<SingleFieldDto> result = gatewayServerClient.allUsersCount(bearerToken);
        checkResult(result);
        return result.getData().getResponse().toString();
    }

}
