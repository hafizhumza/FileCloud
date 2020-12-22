package com.filecloud.authserver.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForgotPasswordVerifiedDto {

	private Long userId;
	private String token;

}
