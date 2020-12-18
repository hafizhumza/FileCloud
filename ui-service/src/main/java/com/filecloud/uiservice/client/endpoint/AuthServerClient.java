package com.filecloud.uiservice.client.endpoint;

import com.filecloud.uiservice.client.response.LoginDto;
import com.filecloud.uiservice.client.response.UserDto;
import com.filecloud.uiservice.constant.UrlConst;
import com.filecloud.uiservice.response.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;


@FeignClient(contextId = "AuthServer", name = "GatewayServer")
public interface AuthServerClient {

    @PostMapping(value = "/aus/oauth/token", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    LoginDto login(@RequestHeader("Authorization") String basicAuth, Map<String, ?> params);

    @GetMapping(UrlConst.URL_AUTH_SERVER + "userinfo")
    Result<UserDto> userinfo(@RequestHeader("Authorization") String bearerToken);

}
