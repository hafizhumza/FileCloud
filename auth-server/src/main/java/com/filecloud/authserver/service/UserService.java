package com.filecloud.authserver.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.filecloud.authserver.constant.ConstUtil;
import com.filecloud.authserver.exception.RecordNotFoundException;
import com.filecloud.authserver.model.db.AuthUser;
import com.filecloud.authserver.model.db.Role;
import com.filecloud.authserver.model.dto.RegisterUserDto;
import com.filecloud.authserver.model.dto.ResponseUserDto;
import com.filecloud.authserver.model.dto.SingleIdRequestDto;
import com.filecloud.authserver.repository.UserRepository;
import com.filecloud.authserver.util.Util;


@Service
public class UserService extends BaseService {

	private final RoleService roleService;

	private final OAuthAccessTokenService oAuthAccessTokenService;

	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	@Autowired
	public UserService(UserRepository userRepository, RoleService roleService, PasswordEncoder passwordEncoder, OAuthAccessTokenService oAuthAccessTokenService) {
		this.userRepository = userRepository;
		this.roleService = roleService;
		this.passwordEncoder = passwordEncoder;
		this.oAuthAccessTokenService = oAuthAccessTokenService;
	}

	public void registerUser(RegisterUserDto userDto) {
		Role role = roleService.findByName(ConstUtil.ROLE_USER);

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

	public List<ResponseUserDto> findAllUsers() {
		List<AuthUser> dbUsers = userRepository.findAll();

		if (Util.isValidList(dbUsers))
			return dbUsers
					.stream()
					.filter(user -> {
						// Admin will ignore
						for (Role role : user.getRoles())
							if (role.getName().equals(ConstUtil.ROLE_USER))
								return true;
						return false;
					})
					.map(u -> new ResponseUserDto(u.getId(), u.getFullName(), u.getEmail(), u.isAccountNonLocked()))
					.collect(Collectors.toList());

		return new ArrayList<>();
	}

	public void enableUser(SingleIdRequestDto dto) {
		AuthUser authUser = userRepository.findById(dto.getId()).orElseThrow(() -> new RecordNotFoundException("User not found"));
		authUser.setAccountNonLocked(true);
		userRepository.save(authUser);
	}

	public void disableUser(SingleIdRequestDto dto) {
		AuthUser authUser = userRepository.findById(dto.getId()).orElseThrow(() -> new RecordNotFoundException("User not found"));

		oAuthAccessTokenService.deleteAccessAndRefreshToken(authUser.getEmail());
		authUser.setAccountNonLocked(false);
		userRepository.save(authUser);
	}

	public void deleteUser(SingleIdRequestDto dto) {
		AuthUser authUser = userRepository.findById(dto.getId()).orElseThrow(() -> new RecordNotFoundException("User not found"));

		oAuthAccessTokenService.deleteAccessAndRefreshToken(authUser.getEmail());
		userRepository.delete(authUser);
	}

	public List<ResponseUserDto> findAllActiveUsers() {
		List<AuthUser> dbUsers = userRepository.findByAccountNonLocked(true);

		if (Util.isValidList(dbUsers))
			return dbUsers
					.stream()
					.filter(u -> {
						for (Role role : u.getRoles())
							if (role.getName().equals(ConstUtil.ROLE_USER))
								return true;
						return false;
					})
					.map(u -> new ResponseUserDto(u.getId(), u.getFullName(), u.getEmail(), null))
					.collect(Collectors.toList());

		return new ArrayList<>();
	}

	public ResponseUserDto getUser(long userId) {
		AuthUser user = userRepository.findById(userId).orElseThrow(() -> new RecordNotFoundException("User not found"));
		return new ResponseUserDto(user);
	}
}
