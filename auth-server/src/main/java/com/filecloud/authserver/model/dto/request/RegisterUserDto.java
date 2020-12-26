
package com.filecloud.authserver.model.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserDto {

	@NotEmpty(message = "Name cannot be empty")
	@NotNull(message = "Name cannot be empty")
	private String fullName;

	@Email(message = "Invalid email")
	private String email;

	@NotEmpty(message = "Password cannot be empty")
	@NotNull(message = "Password cannot be empty")
	private String password;

}
