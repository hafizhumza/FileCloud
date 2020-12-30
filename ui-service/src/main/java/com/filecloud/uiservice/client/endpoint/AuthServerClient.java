package com.filecloud.uiservice.client.endpoint;

import com.filecloud.uiservice.client.request.*;
import com.filecloud.uiservice.client.response.ForgotPasswordVerifiedResponse;
import com.filecloud.uiservice.client.response.LoginResponse;
import com.filecloud.uiservice.client.response.SingleFieldResponse;
import com.filecloud.uiservice.client.response.UserResponse;
import com.filecloud.uiservice.constant.UiConst;
import com.filecloud.uiservice.response.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@FeignClient(contextId = "AuthServer", name = "GatewayServer")
public interface AuthServerClient {

    @PostMapping(value = "/aus/oauth/token", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    LoginResponse login(@RequestHeader("Authorization") String basicAuth, Map<String, ?> params);

    @GetMapping(UiConst.URL_AUTH_SERVER + "userinfo")
    Result<UserResponse> userinfo(@RequestHeader("Authorization") String bearerToken);

    @GetMapping(UiConst.URL_AUTH_SERVER + "user/{userId}")
    Result<UserResponse> getUser(@RequestHeader("Authorization") String bearerToken, @PathVariable long userId);

    @PostMapping(UiConst.URL_AUTH_SERVER + "register")
    Result<String> register(@RequestBody RegisterUserRequest request);

    @PostMapping(UiConst.URL_AUTH_SERVER + "update-profile")
    Result<String> updateProfile(@RequestHeader("Authorization") String bearerToken, @RequestBody UpdateUserRequest request);

    @PostMapping(UiConst.URL_AUTH_SERVER + "forgot-password")
    Result<String> forgotPassword(@RequestBody ForgotPasswordRequest request);

    @PostMapping(UiConst.URL_AUTH_SERVER + "verify-forgot-password-token/{token}")
    Result<ForgotPasswordVerifiedResponse> verifyForgotPasswordToken(@PathVariable String token);

    @PostMapping(UiConst.URL_AUTH_SERVER + "change-forgot-password")
    Result<String> changeForgotPassword(@RequestBody ChangeForgotPasswordRequest request);

    @PostMapping(UiConst.URL_AUTH_SERVER + "change-password")
    Result<String> changePassword(@RequestHeader("Authorization") String bearerToken, @RequestBody ChangePasswordRequest request);

    @GetMapping(UiConst.URL_AUTH_SERVER + "list-active-users")
    Result<List<UserResponse>> listActiveUsers(@RequestHeader("Authorization") String bearerToken);

    @GetMapping(UiConst.URL_AUTH_SERVER + "active-users-exclude-self-and-admin")
    Result<List<UserResponse>> activeUsersExcludeSelfAndAdmin(@RequestHeader("Authorization") String bearerToken);

    @GetMapping(UiConst.URL_AUTH_SERVER + "active-users-count")
    Result<SingleFieldResponse> activeUserCount(@RequestHeader("Authorization") String bearerToken);

}
