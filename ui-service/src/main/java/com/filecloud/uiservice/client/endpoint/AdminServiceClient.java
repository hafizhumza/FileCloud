package com.filecloud.uiservice.client.endpoint;

import com.filecloud.uiservice.client.request.IdDto;
import com.filecloud.uiservice.client.response.SingleFieldDto;
import com.filecloud.uiservice.constant.UrlConst;
import com.filecloud.uiservice.response.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;


@FeignClient(contextId = "AdminService", name = "GatewayServer", path = UrlConst.URL_ADMIN_SERVICE)
public interface AdminServiceClient {

    @GetMapping("list-users")
    Result<?> listUsers(@RequestHeader("Authorization") String bearerToken);

    @PostMapping("enable-user")
    Result<?> enableUser(@RequestHeader("Authorization") String bearerToken, IdDto dto);

    @PostMapping("disable-user")
    Result<?> disableUser(@RequestHeader("Authorization") String bearerToken, IdDto dto);

    @PostMapping("delete-user")
    Result<?> deleteUser(@RequestHeader("Authorization") String bearerToken, IdDto dto);

    @GetMapping("user/{userId}")
    Result<?> getUser(@RequestHeader("Authorization") String bearerToken, @PathVariable long userId);

    @GetMapping("active-users-count")
    Result<SingleFieldDto> activeUserCount(@RequestHeader("Authorization") String bearerToken);

    @GetMapping("inactive-users-count")
    Result<SingleFieldDto> inActiveUserCount(@RequestHeader("Authorization") String bearerToken);

    @GetMapping("all-users-count")
    Result<SingleFieldDto> allUsersCount(@RequestHeader("Authorization") String bearerToken);

}
