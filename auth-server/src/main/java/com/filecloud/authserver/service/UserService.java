package com.filecloud.authserver.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.filecloud.authserver.client.dto.ForgotPasswordEmailDto;
import com.filecloud.authserver.client.endpoint.EmailServiceClient;
import com.filecloud.authserver.constant.ConstUtil;
import com.filecloud.authserver.exception.InvalidAccessException;
import com.filecloud.authserver.exception.RecordNotFoundException;
import com.filecloud.authserver.model.db.AuthUser;
import com.filecloud.authserver.model.db.ForgotPassword;
import com.filecloud.authserver.model.db.Role;
import com.filecloud.authserver.model.dto.request.ChangeForgotPasswordRequest;
import com.filecloud.authserver.model.dto.request.ChangePasswordDto;
import com.filecloud.authserver.model.dto.request.EmailRequestDto;
import com.filecloud.authserver.model.dto.request.RegisterUserDto;
import com.filecloud.authserver.model.dto.request.SingleIdRequestDto;
import com.filecloud.authserver.model.dto.request.UpdateUserRequest;
import com.filecloud.authserver.model.dto.response.ForgotPasswordVerifiedDto;
import com.filecloud.authserver.model.dto.response.ResponseUserDto;
import com.filecloud.authserver.model.dto.response.SingleFieldResponse;
import com.filecloud.authserver.properties.AuthServerProperties;
import com.filecloud.authserver.repository.UserRepository;
import com.filecloud.authserver.response.Result;
import com.filecloud.authserver.security.dto.AuthUserDetail;
import com.filecloud.authserver.security.util.AuthUtil;
import com.filecloud.authserver.util.Util;


@Service
public class UserService extends BaseService {

	private final RoleService roleService;

	private final OAuthAccessTokenService oAuthAccessTokenService;

	private final ForgotPasswordService forgotPasswordService;

	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	private final AuthServerProperties authServerProperties;

	private final EmailServiceClient emailServiceClient;

	@Autowired
	public UserService(UserRepository userRepository, RoleService roleService, PasswordEncoder passwordEncoder, OAuthAccessTokenService oAuthAccessTokenService, ForgotPasswordService forgotPasswordService, AuthServerProperties authServerProperties, EmailServiceClient emailServiceClient) {
		this.userRepository = userRepository;
		this.roleService = roleService;
		this.passwordEncoder = passwordEncoder;
		this.oAuthAccessTokenService = oAuthAccessTokenService;
		this.forgotPasswordService = forgotPasswordService;
		this.authServerProperties = authServerProperties;
		this.emailServiceClient = emailServiceClient;
	}

	public void registerUser(RegisterUserDto userDto) {
		Optional<AuthUser> existingUser = userRepository.findByEmail(userDto.getEmail());

		if (existingUser.isPresent())
			duplicateEmail();

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

	public List<ResponseUserDto> findAllAdminUsers() {
		List<AuthUser> dbUsers = userRepository.findAll();

		if (Util.isValidList(dbUsers))
			return getResponseUserDtoExcludeUser(dbUsers);

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

	private List<ResponseUserDto> getResponseUserDtoExcludeUser(List<AuthUser> dbUsers) {
		return dbUsers
				.stream()
				.filter(u -> {
					for (Role role : u.getRoles())
						if (role.getName().equals(ConstUtil.ROLE_ADMIN))
							return true;
					return false;
				})
				.map(u -> new ResponseUserDto(u.getId(), u.getFullName(), u.getEmail(), Util.removeRolePrefix(ConstUtil.ROLE_ADMIN), u.isAccountNonLocked()))
				.collect(Collectors.toList());
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
				.map(u -> new ResponseUserDto(u.getId(), u.getFullName(), u.getEmail(), Util.removeRolePrefix(ConstUtil.ROLE_USER), u.isAccountNonLocked()))
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

	private ForgotPassword getVerifiedForgotPassword(String token) {
		ForgotPassword forgotPassword = forgotPasswordService.findByTokenOrElseThrow(token);

		if (forgotPassword.getAvailed())
			invalidAccess("Link is expired");

		long expiryDaysMillis = Util.getDaysMillis(authServerProperties.security().getForgotPasswordLinkExpiryDays());
		long createDate = forgotPassword.getCreateDate();
		long expiryDate = expiryDaysMillis + createDate;
		long currentTimeMillis = System.currentTimeMillis();

		if (currentTimeMillis > expiryDate)
			invalidAccess("Link is expired");

		return forgotPassword;
	}

	public ForgotPasswordVerifiedDto verifyForgotPasswordToken(String token) {
		ForgotPassword forgotPassword = getVerifiedForgotPassword(token);
		return new ForgotPasswordVerifiedDto(forgotPassword.getUserId(), forgotPassword.getToken());
	}

	public void changeForgotPassword(ChangeForgotPasswordRequest dto) {
		ForgotPassword forgotPassword = this.getVerifiedForgotPassword(dto.getToken());

		if (dto.getId() != forgotPassword.getUserId())
			invalidAccess("Unauthorize user");

		if (!dto.getPassword().equals(dto.getConfirmPassword()))
			invalidInput("Passwords not match");

		AuthUser user = userRepository.findById(dto.getId()).orElseThrow(RecordNotFoundException::new);
		user.setPassword(passwordEncoder.encode(dto.getPassword()));
		userRepository.save(user);
		forgotPassword.setAvailed(true);
		forgotPasswordService.save(forgotPassword);
		oAuthAccessTokenService.deleteAccessAndRefreshToken(user.getEmail());
	}

	public void changePassword(ChangePasswordDto dto) {
		String email = ((AuthUserDetail) AuthUtil.getPrincipal()).getUsername();
		AuthUser user = userRepository.findByEmail(email).orElseThrow(() -> new RecordNotFoundException("User not found with associated email address"));

		if (!user.isAccountNonLocked())
			invalidAccess("User account is locked");

		if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword()))
			invalidInput("Invalid current password");

		if (!dto.getNewPassword().equals(dto.getConfirmPassword()))
			invalidInput("Passwords not match");

		user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
		userRepository.save(user);
		oAuthAccessTokenService.deleteAccessAndRefreshToken(user.getEmail());
	}

	public void forgotPassword(EmailRequestDto dto) {
		AuthUser user = userRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new RecordNotFoundException("No account found with given email address"));
		String token = Util.getRandomUUID();

		Optional<ForgotPassword> optionalForgotPassword = forgotPasswordService.findByUserId(user.getId());
		ForgotPassword forgotPassword;

		if (optionalForgotPassword.isPresent()) {
			forgotPassword = optionalForgotPassword.get();
		} else {
			forgotPassword = new ForgotPassword();
			forgotPassword.setUserId(user.getId());
		}

		forgotPassword.setAvailed(false);
		forgotPassword.setToken(token);
		forgotPassword.setCreateDate(System.currentTimeMillis());
		forgotPasswordService.save(forgotPassword);

		ForgotPasswordEmailDto emailDto = new ForgotPasswordEmailDto();
		emailDto.setName(user.getFullName());
		emailDto.setReceiverEmail(user.getEmail());
		emailDto.setExpiryDays(authServerProperties.security().getForgotPasswordLinkExpiryDays());
		emailDto.setUrl(getForgotPasswordUrl(token));
		Result<?> result = emailServiceClient.emailForgotPassword(emailDto);

		if (!result.isSuccess())
			error(result.getMessage());
	}

	private String getForgotPasswordUrl(String token) {
		return ServletUriComponentsBuilder.fromCurrentContextPath()
				.scheme(authServerProperties.getUiServiceScheme())
				.host(authServerProperties.getUiServiceHost())
				.port(authServerProperties.getUiServicePort())
				.path(authServerProperties.getUiServiceForgotPasswordUrl())
				.path("/" + token)
				.toUriString();
	}

	public void updateUser(UpdateUserRequest request) {
		AuthUserDetail user = AuthUtil.getPrincipal();
		AuthUser dbUser = userRepository.findById(user.getUserId()).orElseThrow(InvalidAccessException::new);
		dbUser.setFullName(request.getName());
		dbUser.setEmail(request.getEmail());
		userRepository.save(dbUser);
	}

}
