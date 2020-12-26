package com.filecloud.authserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.filecloud.authserver.model.dto.request.SingleIdRequestDto;
import com.filecloud.authserver.response.Response.Status;
import com.filecloud.authserver.response.Result;
import com.filecloud.authserver.security.role.Admin;
import com.filecloud.authserver.service.UserService;


@Admin
@Transactional(readOnly = true)
@RequestMapping("api/v1/admin")
@RestController
public class AdminController extends BaseController {

	private final UserService userService;

	@Autowired
	public AdminController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/list-users")
	public Result<?> users() {
		return sendSuccessResponse(Status.ALL_OK, userService.findAllUsers());
	}

	@GetMapping("/list-active-users")
	public Result<?> activeUsers() {
		return sendSuccessResponse(Status.ALL_OK, userService.findActiveUsers());
	}

	@GetMapping("/list-inactive-users")
	public Result<?> inactiveusers() {
		return sendSuccessResponse(Status.ALL_OK, userService.findInActiveUsers());
	}

	@Transactional
	@PostMapping("/enable-user")
	public Result<?> enableUser(@RequestBody SingleIdRequestDto dto) {
		userService.enableUser(dto);
		return sendSuccessResponse(Status.ALL_OK, "User enabled successfully!");
	}

	@Transactional
	@PostMapping("/disable-user")
	public Result<?> disableUser(@RequestBody SingleIdRequestDto dto) {
		userService.disableUser(dto);
		return sendSuccessResponse(Status.ALL_OK, "User disabled successfully!");
	}

	@Transactional
	@PostMapping("/delete-user")
	public Result<?> deleteUser(@RequestBody SingleIdRequestDto dto) {
		userService.deleteUser(dto);
		return sendSuccessResponse(Status.ALL_OK, "User deleted successfully!");
	}

	@GetMapping("/inactive-users-count")
	public Result<?> inActiveUserCount() {
		return sendSuccessResponse(Status.ALL_OK, userService.getInActiveUserCount());
	}

	@GetMapping("/all-users-count")
	public Result<?> allUsersCount() {
		return sendSuccessResponse(Status.ALL_OK, userService.getAllUsersCount());
	}

	@GetMapping("/list-role-admin")
	public Result<?> listRoleAdmin() {
		return sendSuccessResponse(Status.ALL_OK, userService.findAllAdminUsers());
	}

	@GetMapping("/list-role-user")
	public Result<?> listRoleUser() {
		return sendSuccessResponse(Status.ALL_OK, userService.findAllUsersExcludeAdmin());
	}

}
