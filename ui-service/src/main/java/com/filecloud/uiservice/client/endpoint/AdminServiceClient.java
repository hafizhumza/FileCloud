package com.filecloud.uiservice.client.endpoint;

import com.filecloud.uiservice.client.request.IdRequest;
import com.filecloud.uiservice.client.response.SingleFieldResponse;
import com.filecloud.uiservice.client.response.UserResponse;
import com.filecloud.uiservice.constant.UiConst;
import com.filecloud.uiservice.response.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;


@FeignClient(contextId = "AdminService", name = "GatewayServer", path = UiConst.URL_ADMIN_SERVICE)
public interface AdminServiceClient {

    @GetMapping("list-users")
    Result<List<UserResponse>> listUsers(@RequestHeader("Authorization") String bearerToken);

    @GetMapping("list-active-users")
    Result<List<UserResponse>> activeUsers(@RequestHeader("Authorization") String bearerToken);

    @GetMapping("list-inactive-users")
    Result<List<UserResponse>> inactiveusers(@RequestHeader("Authorization") String bearerToken);

    @PostMapping("enable-user")
    Result<String> enableUser(@RequestHeader("Authorization") String bearerToken, IdRequest dto);

    @PostMapping("disable-user")
    Result<String> disableUser(@RequestHeader("Authorization") String bearerToken, IdRequest dto);

    @PostMapping("delete-user")
    Result<String> deleteUser(@RequestHeader("Authorization") String bearerToken, IdRequest dto);

    @GetMapping("inactive-users-count")
    Result<SingleFieldResponse> inActiveUserCount(@RequestHeader("Authorization") String bearerToken);

    @GetMapping("all-users-count")
    Result<SingleFieldResponse> allUsersCount(@RequestHeader("Authorization") String bearerToken);

    @GetMapping("list-role-admin")
    Result<List<UserResponse>> listRoleAdmin(@RequestHeader("Authorization") String bearerToken);

    @GetMapping("list-role-user")
    Result<List<UserResponse>> listRoleUser(@RequestHeader("Authorization") String bearerToken);

}
