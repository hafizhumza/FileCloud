package com.filecloud.adminservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.filecloud.adminservice.model.dto.SingleIdRequestDto;
import com.filecloud.adminservice.response.Result;


@FeignClient(name = "AuthServer", path = "api/v1/")
public interface AuthServerClient {

	@GetMapping("admin/list-users")
	Result<?> listUsers(@RequestHeader("Authorization") String bearerToken);

	@GetMapping("admin/list-active-users")
	Result<?> activeUsers(@RequestHeader("Authorization") String bearerToken);

	@GetMapping("admin/list-inactive-users")
	Result<?> inactiveusers(@RequestHeader("Authorization") String bearerToken);

	@PostMapping("admin/enable-user")
	Result<?> enableUser(@RequestHeader("Authorization") String bearerToken, SingleIdRequestDto dto);

	@PostMapping("admin/disable-user")
	Result<?> disableUser(@RequestHeader("Authorization") String bearerToken, SingleIdRequestDto dto);

	@PostMapping("admin/delete-user")
	Result<?> deleteUser(@RequestHeader("Authorization") String bearerToken, SingleIdRequestDto dto);

	@GetMapping("admin/active-users-count")
	Result<?> activeUserCount(@RequestHeader("Authorization") String bearerToken);

	@GetMapping("admin/inactive-users-count")
	Result<?> inActiveUserCount(@RequestHeader("Authorization") String bearerToken);

	@GetMapping("admin/all-users-count")
	Result<?> allUsersCount(@RequestHeader("Authorization") String bearerToken);

	@GetMapping("user/{userId}")
	Result<?> getUser(@RequestHeader("Authorization") String bearerToken, @PathVariable long userId);

	@GetMapping("admin/list-role-admin")
	Result<?> listRoleAdmin(@RequestHeader("Authorization") String bearerToken);

	@GetMapping("admin/list-role-user")
	Result<?> listRoleUser(@RequestHeader("Authorization") String bearerToken);

}
