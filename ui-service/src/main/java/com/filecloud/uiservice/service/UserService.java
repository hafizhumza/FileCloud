package com.filecloud.uiservice.service;

import com.filecloud.uiservice.client.endpoint.AuthServerClient;
import com.filecloud.uiservice.client.response.SingleFieldResponse;
import com.filecloud.uiservice.client.response.UserResponse;
import com.filecloud.uiservice.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserService extends BaseService {

    private final AuthServerClient authServerClient;

    @Autowired
    public UserService(AuthServerClient authServerClient) {
        this.authServerClient = authServerClient;
    }

    public Result<List<UserResponse>> listActiveUsers(String token) {
        Result<List<UserResponse>> result = authServerClient.listActiveUsers(token);
        logIfError(result);
        return result;
    }

    public String activeUserCount(String bearerToken) {
        Result<SingleFieldResponse> result = authServerClient.activeUserCount(bearerToken);
        logIfError(result);
        return result.getData().getResponse().toString();
    }

}
