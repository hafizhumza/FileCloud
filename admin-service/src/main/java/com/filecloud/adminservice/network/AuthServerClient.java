package com.filecloud.adminservice.network;

import com.filecloud.adminservice.model.dto.SingleIdRequestDto;
import com.filecloud.adminservice.response.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "AuthServer", path = "api/v1/")
public interface AuthServerClient {

    @GetMapping("admin/list-users")
    Result<?> listUsers(@RequestHeader("Authorization") String bearerToken);

    @PostMapping("admin/enable-user")
    Result<?> enableUser(@RequestHeader("Authorization") String bearerToken, SingleIdRequestDto dto);

    @PostMapping("admin/disable-user")
    Result<?> disableUser(@RequestHeader("Authorization") String bearerToken, SingleIdRequestDto dto);

    @PostMapping("admin/delete-user")
    Result<?> deleteUser(@RequestHeader("Authorization") String bearerToken, SingleIdRequestDto dto);

    @GetMapping("user/{userId}")
    Result<?> getUser(@RequestHeader("Authorization") String bearerToken, @PathVariable long userId);

}
