package com.filecloud.uiservice.client.endpoint;

import com.filecloud.uiservice.client.request.IdRequest;
import com.filecloud.uiservice.client.response.SingleFieldResponse;
import com.filecloud.uiservice.client.response.UserDto;
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
    Result<List<UserDto>> listUsers(@RequestHeader("Authorization") String bearerToken);

    @GetMapping("list-active-users")
    Result<List<UserDto>> activeUsers(@RequestHeader("Authorization") String bearerToken);

    @GetMapping("list-inactive-users")
    Result<List<UserDto>> inactiveusers(@RequestHeader("Authorization") String bearerToken);

    @PostMapping("enable-user")
    Result<String> enableUser(@RequestHeader("Authorization") String bearerToken, IdRequest dto);

    @PostMapping("disable-user")
    Result<String> disableUser(@RequestHeader("Authorization") String bearerToken, IdRequest dto);

    @PostMapping("delete-user")
    Result<String> deleteUser(@RequestHeader("Authorization") String bearerToken, IdRequest dto);

    @GetMapping("active-users-count")
    Result<SingleFieldResponse> activeUserCount(@RequestHeader("Authorization") String bearerToken);

    @GetMapping("inactive-users-count")
    Result<SingleFieldResponse> inActiveUserCount(@RequestHeader("Authorization") String bearerToken);

    @GetMapping("all-users-count")
    Result<SingleFieldResponse> allUsersCount(@RequestHeader("Authorization") String bearerToken);

    @GetMapping("list-role-admin")
    Result<List<UserDto>> listRoleAdmin(@RequestHeader("Authorization") String bearerToken);

    @GetMapping("list-role-user")
    Result<List<UserDto>> listRoleUser(@RequestHeader("Authorization") String bearerToken);

}
