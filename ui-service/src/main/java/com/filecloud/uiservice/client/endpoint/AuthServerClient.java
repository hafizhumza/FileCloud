package com.filecloud.uiservice.client.endpoint;

import com.filecloud.uiservice.client.response.LoginResponse;
import com.filecloud.uiservice.client.response.ResponseUserDto;
import com.filecloud.uiservice.response.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;


@FeignClient(name = "GatewayServer", path = "/aus/")
public interface AuthServerClient {

    @PostMapping(value = "oauth/token", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    LoginResponse login(
            @RequestHeader("Authorization") String basicAuth,
            Map<String, ?> params
    );

    @GetMapping("api/v1/userinfo")
    Result<ResponseUserDto> userinfo(@RequestHeader("Authorization") String bearerToken);

}
