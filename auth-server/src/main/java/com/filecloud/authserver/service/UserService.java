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
import com.filecloud.authserver.model.dto.request.RegisterUserDto;
import com.filecloud.authserver.model.dto.request.SingleIdRequestDto;
import com.filecloud.authserver.model.dto.response.ResponseUserDto;
import com.filecloud.authserver.model.dto.response.SingleFieldResponse;
import com.filecloud.authserver.repository.UserRepository;
import com.filecloud.authserver.security.dto.AuthUserDetail;
import com.filecloud.authserver.security.util.AuthUtil;
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
		authUser.setFullName(userDto.getFullName().trim());
		authUser.setEmail(userDto.getEmail().trim());
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
		return mapToResponseUserDto(dbUsers);
	}

	private List<ResponseUserDto> mapToResponseUserDto(List<AuthUser> dbUsers) {
		if (!Util.isValidList(dbUsers))
			return new ArrayList<ResponseUserDto>();

		return dbUsers
				.stream()
				.map(u -> {
					String role = Util.removeRolePrefix(u.getRoles().stream().findFirst().get().getName());
					return new ResponseUserDto(u.getId(), u.getFullName(), u.getEmail(), role, u.isAccountNonLocked());
				})
				.collect(Collectors.toList());
	}

	public List<ResponseUserDto> findAllUsersExcludeAdmin() {
		List<AuthUser> dbUsers = userRepository.findAll();

		if (Util.isValidList(dbUsers))
			return getResponseUserDtoExcludeAdmin(dbUsers);

		return new ArrayList<>();
	}

	public void enableUser(SingleIdRequestDto dto) {
		AuthUser authUser = shouldNotAdmin(dto.getId());
		authUser.setAccountNonLocked(true);
		userRepository.save(authUser);
	}

	public void disableUser(SingleIdRequestDto dto) {
		AuthUser authUser = shouldNotAdmin(dto.getId());
		oAuthAccessTokenService.deleteAccessAndRefreshToken(authUser.getEmail());
		authUser.setAccountNonLocked(false);
		userRepository.save(authUser);
	}

	public void deleteUser(SingleIdRequestDto dto) {
		AuthUser authUser = shouldNotAdmin(dto.getId());
		oAuthAccessTokenService.deleteAccessAndRefreshToken(authUser.getEmail());
		userRepository.delete(authUser);
	}

	public List<ResponseUserDto> findActiveUsers() {
		List<AuthUser> dbUsers = userRepository.findByAccountNonLocked(true);
		return mapToResponseUserDto(dbUsers);
	}

	public List<ResponseUserDto> findInActiveUsers() {
		List<AuthUser> dbUsers = userRepository.findByAccountNonLocked(false);
		return mapToResponseUserDto(dbUsers);
	}

	public List<ResponseUserDto> findAllActiveUsersExcludeAdmin() {
		List<AuthUser> dbUsers = userRepository.findByAccountNonLocked(true);

		if (Util.isValidList(dbUsers))
			return getResponseUserDtoExcludeAdmin(dbUsers);

		return new ArrayList<>();
	}

	public ResponseUserDto getUser(long userId) {
		AuthUser user = getUserOrElseThrowException(userId);
		return new ResponseUserDto(user);
	}

	public ResponseUserDto getCurrentUser() {
		AuthUserDetail user = AuthUtil.getPrincipal();
		return new ResponseUserDto(user);
	}

	private AuthUser shouldNotAdmin(long id) {
		AuthUser authUser = getUserOrElseThrowException(id);

		if (isAdmin(authUser))
			invalidAccess("Cannot process on admin user");

		return authUser;
	}

	private List<ResponseUserDto> getResponseUserDtoExcludeAdmin(List<AuthUser> dbUsers) {
		return dbUsers
				.stream()
				.filter(u -> {
					for (Role role : u.getRoles())
						if (role.getName().equals(ConstUtil.ROLE_USER))
							return true;
					return false;
				})
				.map(u -> new ResponseUserDto(u.getId(), u.getFullName(), u.getEmail(), ConstUtil.ROLE_USER, u.isAccountNonLocked()))
				.collect(Collectors.toList());
	}

	private boolean isAdmin(AuthUser authUser) {
		return authUser.getRoles().stream().anyMatch(r -> r.getName().equals(ConstUtil.ROLE_ADMIN));
	}

	private AuthUser getUserOrElseThrowException(long id) {
		return userRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("User not found"));
	}

	public SingleFieldResponse getActiveUserCount() {
		return new SingleFieldResponse(userRepository.countByAccountNonLocked(true));
	}

	public SingleFieldResponse getInActiveUserCount() {
		return new SingleFieldResponse(userRepository.countByAccountNonLocked(false));
	}

	public SingleFieldResponse getAllUsersCount() {
		return new SingleFieldResponse(userRepository.count());
	}
}
