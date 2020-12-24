package com.filecloud.uiservice.client.endpoint;

import com.filecloud.uiservice.client.request.ChangeForgotPasswordRequest;
import com.filecloud.uiservice.client.request.ChangePasswordRequest;
import com.filecloud.uiservice.client.request.ForgotPasswordRequest;
import com.filecloud.uiservice.client.request.RegisterUserRequest;
import com.filecloud.uiservice.client.response.ForgotPasswordVerifiedResponse;
import com.filecloud.uiservice.client.response.LoginResponse;
import com.filecloud.uiservice.client.response.UserDto;
import com.filecloud.uiservice.constant.UiConst;
import com.filecloud.uiservice.response.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@FeignClient(contextId = "AuthServer", name = "GatewayServer")
public interface AuthServerClient {

    @PostMapping(value = "/aus/oauth/token", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    LoginResponse login(@RequestHeader("Authorization") String basicAuth, Map<String, ?> params);

    @GetMapping(UiConst.URL_AUTH_SERVER + "userinfo")
    Result<UserDto> userinfo(@RequestHeader("Authorization") String bearerToken);

    @GetMapping(UiConst.URL_AUTH_SERVER + "user/{userId}")
    Result<UserDto> getUser(@RequestHeader("Authorization") String bearerToken, @PathVariable long userId);

    @PostMapping(UiConst.URL_AUTH_SERVER + "register")
    Result<?> register(@RequestHeader("Authorization") String bearerToken, @RequestBody RegisterUserRequest request);

    @PostMapping(UiConst.URL_AUTH_SERVER + "forgot-password")
    Result<String> forgotPassword(@RequestBody ForgotPasswordRequest request);

    @PostMapping(UiConst.URL_AUTH_SERVER + "verify-forgot-password-token/{token}")
    Result<ForgotPasswordVerifiedResponse> verifyForgotPasswordToken(@PathVariable String token);

    @PostMapping(UiConst.URL_AUTH_SERVER + "change-forgot-password")
    Result<String> changeForgotPassword(@RequestBody ChangeForgotPasswordRequest request);

    @PostMapping(UiConst.URL_AUTH_SERVER + "change-password")
    Result<?> changePassword(@RequestHeader("Authorization") String bearerToken, @RequestBody ChangePasswordRequest request);

    @GetMapping(UiConst.URL_AUTH_SERVER + "list-active-users")
    Result<?> listActiveUsers(@RequestHeader("Authorization") String bearerToken);

}
