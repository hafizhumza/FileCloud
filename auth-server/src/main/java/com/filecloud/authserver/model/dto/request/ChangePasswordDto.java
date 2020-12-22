
package com.filecloud.authserver.model.dto.request;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordDto {

	@NotNull(message = "Old password cannot be null")
	private String currentPassword;

	@NotNull(message = "New password cannot be null")
	private String newPassword;

	@NotNull(message = "Confirm password cannot be null")
	private String confirmPassword;

}
