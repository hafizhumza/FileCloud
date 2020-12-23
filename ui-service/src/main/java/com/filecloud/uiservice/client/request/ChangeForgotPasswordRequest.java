
package com.filecloud.uiservice.client.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeForgotPasswordRequest {

	@Min(value = 1)
	private long id;

	@NotNull(message = "Password cannot be null")
	private String password;

	@NotNull(message = "Confirm password cannot be null")
	private String confirmPassword;

	@NotNull(message = "Token cannot be null")
	private String token;

}
