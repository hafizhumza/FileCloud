package com.filecloud.authserver.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.stereotype.Service;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import com.filecloud.authserver.model.db.AuthUser;
import com.filecloud.authserver.model.db.Role;
import com.filecloud.authserver.model.dto.LogInDto;
import com.filecloud.authserver.model.dto.RegisterUserDto;
import com.filecloud.authserver.model.dto.RequestUserDto;
import com.filecloud.authserver.model.dto.ResponseUserDto;
import com.filecloud.authserver.repository.UserRepository;
import com.filecloud.authserver.security.util.AuthUtil;
import com.filecloud.authserver.util.AppConstants;
import com.filecloud.authserver.util.Util;


@Service
public class UserService {

	private final RoleService roleService;

	private final OAuthAccessTokenService oAuthAccessTokenService;

	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	private final TokenEndpoint tokenEndpoint;

	@Autowired
	public UserService(UserRepository userRepository, RoleService roleService, PasswordEncoder passwordEncoder, TokenEndpoint tokenEndpoint, OAuthAccessTokenService oAuthAccessTokenService) {
		this.userRepository = userRepository;
		this.roleService = roleService;
		this.passwordEncoder = passwordEncoder;
		this.tokenEndpoint = tokenEndpoint;
		this.oAuthAccessTokenService = oAuthAccessTokenService;
	}

	public void registerUser(RegisterUserDto userDto) {
		Role role = roleService.findByName(AppConstants.ROLE_USER);

		AuthUser authUser = new AuthUser();
		authUser.setFullName(userDto.getFullName());
		authUser.setEmail(userDto.getEmail());
		authUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
		authUser.setAccountNonExpired(true);
		authUser.setAccountNonLocked(false);
		authUser.setCredentialsNonExpired(true);
		authUser.setEnabled(true);
		authUser.setRoles(Collections.singletonList(role));

		userRepository.save(authUser);
	}

	public OAuth2AccessToken login(LogInDto logInDto) throws HttpRequestMethodNotSupportedException {
		// First check client type if admin then add scope WRITE, if user then add scope USER and DOCUMENT
		String scope;

		if (isAdmin(logInDto.getEmail()))
			scope = AppConstants.SCOPE_WRITE;
		else
			scope = AppConstants.SCOPE_READ + " " + AppConstants.SCOPE_DOCUMENT;

		User user = new User(logInDto.getClientId(), logInDto.getClientSecret(), true, true, true, true, Collections.emptyList());
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());

		Map<String, String> request = new HashMap<>();
		request.put("username", logInDto.getEmail());
		request.put("password", logInDto.getPassword());
		request.put("grant_type", "password");
		request.put("scope", scope);
		request.put("client_id", logInDto.getClientId());
		request.put("client_secret", logInDto.getClientSecret());
		request.put("clientVersion", "resource");
		request.put("userType", "user");

		return this.tokenEndpoint.postAccessToken(token, request).getBody();
	}

	private boolean isAdmin(String email) {
		Optional<AuthUser> user = userRepository.findByEmail(email);

		return user.map(u -> {
			for (Role role : u.getRoles())
				if (role.getName().equals(AppConstants.ROLE_ADMIN))
					return true;
			return false;
		}).orElseThrow(() -> new BadCredentialsException("Invalid email or password"));
	}

	public List<ResponseUserDto> findAllUsers() {
		List<AuthUser> dbUsers = userRepository.findAll();

		if (Util.isValidList(dbUsers))
			return dbUsers
					.stream()
					.filter(u -> {
						for (Role role : u.getRoles())
							if (role.getName().equals(AppConstants.ROLE_USER))
								return true;
						return false;
					})
					.map(u -> new ResponseUserDto(u.getId(), u.getFullName(), u.getEmail(), u.isAccountNonLocked()))
					.collect(Collectors.toList());

		return new ArrayList<>();
	}

	public void enableUser(RequestUserDto dto) {
		AuthUser authUser = userRepository.findById(dto.getUserId())
				.orElseThrow(() -> new BadCredentialsException("User not found"));

		authUser.setAccountNonLocked(true);
		userRepository.save(authUser);
	}

	public void disableUser(RequestUserDto dto) {
		AuthUser authUser = userRepository.findById(dto.getUserId())
				.orElseThrow(() -> new BadCredentialsException("User not found"));

		oAuthAccessTokenService.deleteUserAccessAndRefreshToken(authUser.getEmail());
		authUser.setAccountNonLocked(false);
		userRepository.save(authUser);
	}

	public void deleteUser(RequestUserDto dto) {
		AuthUser authUser = userRepository.findById(dto.getUserId())
				.orElseThrow(() -> new BadCredentialsException("User not found"));

		oAuthAccessTokenService.deleteUserAccessAndRefreshToken(authUser.getEmail());
		userRepository.delete(authUser);
	}

	public ResponseUserDto getUser() {
		UserDetails userDetails = AuthUtil.getPrincipal();
		Optional<AuthUser> user = userRepository.findByEmail(userDetails.getUsername());

		return user.map(u -> new ResponseUserDto(u.getId(), u.getFullName(), u.getEmail(), u.isAccountNonLocked()))
				.orElseThrow(() -> new BadCredentialsException("User not found"));
	}

	public List<ResponseUserDto> findAllActiveUsers() {
		List<AuthUser> dbUsers = userRepository.findByAccountNonLocked(true);

		if (Util.isValidList(dbUsers))
			return dbUsers
					.stream()
					.filter(u -> {
						for (Role role : u.getRoles())
							if (role.getName().equals(AppConstants.ROLE_USER))
								return true;
						return false;
					})
					.map(u -> new ResponseUserDto(u.getId(), u.getFullName(), u.getEmail(), null))
					.collect(Collectors.toList());

		return new ArrayList<>();
	}
}
