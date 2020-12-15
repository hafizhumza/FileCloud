package com.filecloud.uiservice.client;

import com.filecloud.uiservice.dto.response.LoginResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
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

}
