package com.filecloud.authserver.model.dto.response;

import com.filecloud.authserver.model.db.AuthUser;
import com.filecloud.authserver.security.dto.AuthUserDetail;
import com.filecloud.authserver.util.Util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseUserDto {

	private Long id;
	private String fullName;
	private String email;
	private String userRole;
	private Boolean accountNonLocked;

	public ResponseUserDto(AuthUser user) {
		id = user.getId();
		fullName = user.getFullName();
		email = user.getEmail();
		userRole = Util.removeRolePrefix(user.getRoles().stream().findFirst().get().getName());
		accountNonLocked = user.isAccountNonLocked();
	}

	public ResponseUserDto(AuthUserDetail user) {
		id = user.getUserId();
		fullName = user.getFullName();
		email = user.getUsername();
		userRole = Util.removeRolePrefix(user.getAuthorities().stream().findFirst().get().getAuthority());
		accountNonLocked = user.isAccountNonLocked();
	}

}
