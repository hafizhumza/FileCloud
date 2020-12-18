package com.filecloud.uiservice.client.endpoint;

import com.filecloud.uiservice.client.request.IdDto;
import com.filecloud.uiservice.client.response.LoginDto;
import com.filecloud.uiservice.client.response.SingleFieldDto;
import com.filecloud.uiservice.client.response.UserDto;
import com.filecloud.uiservice.constant.UrlConst;
import com.filecloud.uiservice.response.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;


@FeignClient(name = "GatewayServer")
public interface GatewayServerClient {

    @PostMapping(value = "/aus/oauth/token", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    LoginDto login(@RequestHeader("Authorization") String basicAuth, Map<String, ?> params);

    @GetMapping(UrlConst.URL_AUTH_SERVER + "userinfo")
    Result<UserDto> userinfo(@RequestHeader("Authorization") String bearerToken);

    @GetMapping(UrlConst.URL_ADMIN_SERVICE + "list-users")
    Result<?> listUsers(@RequestHeader("Authorization") String bearerToken);

    @PostMapping(UrlConst.URL_ADMIN_SERVICE + "enable-user")
    Result<?> enableUser(@RequestHeader("Authorization") String bearerToken, IdDto dto);

    @PostMapping(UrlConst.URL_ADMIN_SERVICE + "disable-user")
    Result<?> disableUser(@RequestHeader("Authorization") String bearerToken, IdDto dto);

    @PostMapping(UrlConst.URL_ADMIN_SERVICE + "delete-user")
    Result<?> deleteUser(@RequestHeader("Authorization") String bearerToken, IdDto dto);

    @GetMapping(UrlConst.URL_ADMIN_SERVICE + "user/{userId}")
    Result<?> getUser(@RequestHeader("Authorization") String bearerToken, @PathVariable long userId);

    @GetMapping(UrlConst.URL_ADMIN_SERVICE + "active-users-count")
    Result<SingleFieldDto> activeUserCount(@RequestHeader("Authorization") String bearerToken);

    @GetMapping(UrlConst.URL_ADMIN_SERVICE + "inactive-users-count")
    Result<SingleFieldDto> inActiveUserCount(@RequestHeader("Authorization") String bearerToken);

    @GetMapping(UrlConst.URL_ADMIN_SERVICE + "all-users-count")
    Result<SingleFieldDto> allUsersCount(@RequestHeader("Authorization") String bearerToken);

}
