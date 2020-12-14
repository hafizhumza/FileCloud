package com.filecloud.authserver.model.dto.response;

import com.filecloud.authserver.model.db.AuthUser;
import com.filecloud.authserver.security.dto.AuthUserDetail;

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
	private Boolean accountNonLocked;

	public ResponseUserDto(AuthUser user) {
		id = user.getId();
		fullName = user.getFullName();
		email = user.getEmail();
		accountNonLocked = user.isAccountNonLocked();
	}

	public ResponseUserDto(AuthUserDetail user) {
		id = user.getUserId();
		fullName = user.getFullName();
		email = user.getUsername();
		accountNonLocked = user.isAccountNonLocked();
	}

}
