package com.filecloud.authserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.filecloud.authserver.model.dto.LogInDto;
import com.filecloud.authserver.model.dto.RegisterUserDto;
import com.filecloud.authserver.service.UserService;


@Transactional(readOnly = true)
@RequestMapping("api/v1")
@RestController
public class UserController {

	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	// TODO: Login can be achieved from /oauth/token endpoint as well
	@Transactional
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LogInDto logInDto) throws HttpRequestMethodNotSupportedException {
		return new ResponseEntity<>(userService.login(logInDto), HttpStatus.OK);
	}

	@Transactional
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody RegisterUserDto userDto) {
		userService.registerUser(userDto);
		return new ResponseEntity<>("User registered successfully!", HttpStatus.OK);
	}

	@GetMapping("/user-detail")
	public ResponseEntity<?> detail() {
		return new ResponseEntity<>(userService.getUser(), HttpStatus.OK);
	}

	@GetMapping("/list-active-users")
	public ResponseEntity<?> listActiveUsers() {
		return new ResponseEntity<>(userService.findAllActiveUsers(), HttpStatus.OK);
	}

}
