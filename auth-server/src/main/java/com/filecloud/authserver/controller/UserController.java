package com.filecloud.authserver.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.filecloud.authserver.model.dto.request.ChangeForgotPasswordRequest;
import com.filecloud.authserver.model.dto.request.ChangePasswordDto;
import com.filecloud.authserver.model.dto.request.EmailRequestDto;
import com.filecloud.authserver.model.dto.request.RegisterUserDto;
import com.filecloud.authserver.model.dto.request.UpdateUserRequest;
import com.filecloud.authserver.response.Response.Status;
import com.filecloud.authserver.response.Result;
import com.filecloud.authserver.service.UserService;


@Transactional(readOnly = true)
@RequestMapping("api/v1")
@RestController
public class UserController extends BaseController {

	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@Transactional
	@PostMapping("/register")
	public Result<?> register(@RequestBody RegisterUserDto userDto) {
		userService.registerUser(userDto);
		return sendSuccessResponse(Status.ALL_OK, "Success. We will approve your registration soon.");
	}

	@Transactional
	@PostMapping("/update-profile")
	public Result<?> updateProfile(@RequestBody UpdateUserRequest request) {
		userService.updateUser(request);
		return sendSuccessResponse(Status.ALL_OK, "User updated successfully!");
	}

	@Transactional
	@PostMapping("/forgot-password")
	public Result<?> forgotPassword(@RequestBody @Valid EmailRequestDto dto) {
		userService.forgotPassword(dto);
		return sendSuccessResponse(Status.ALL_OK, "Please check your email address to reset your password");
	}

	@PostMapping("/verify-forgot-password-token/{token}")
	public Result<?> verifyForgotPasswordToken(@PathVariable(required = true) String token) {
		return sendSuccessResponse(Status.ALL_OK, userService.verifyForgotPasswordToken(token));
	}

	@Transactional
	@PostMapping("/change-forgot-password")
	public Result<?> changeForgotPassword(@RequestBody @Valid ChangeForgotPasswordRequest dto) {
		userService.changeForgotPassword(dto);
		return sendSuccessResponse(Status.ALL_OK, "Password change successfully!");
	}

	@Transactional
	@PostMapping("/change-password")
	public Result<?> changePassword(@RequestBody @Valid ChangePasswordDto dto) {
		userService.changePassword(dto);
		return sendSuccessResponse(Status.ALL_OK, "Password changed successfully!");
	}

	@GetMapping("/list-active-users")
	public Result<?> listActiveUsers() {
		return sendSuccessResponse(Status.ALL_OK, userService.findActiveUsers());
	}

	@GetMapping("/user/{userId}")
	public Result<?> getUser(@PathVariable long userId) {
		return sendSuccessResponse(Status.ALL_OK, userService.getUser(userId));
	}

	@GetMapping("/userinfo")
	public Result<?> userInfo() {
		return sendSuccessResponse(Status.ALL_OK, userService.getCurrentUser());
	}

	@GetMapping("/active-users-count")
	public Result<?> activeUserCount() {
		return sendSuccessResponse(Status.ALL_OK, userService.getActiveUserCount());
	}

}
