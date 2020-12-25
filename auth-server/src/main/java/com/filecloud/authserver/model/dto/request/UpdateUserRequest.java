package com.filecloud.authserver.model.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {

	@NotBlank(message = "Name cannot be empty")
	@NotNull(message = "Name cannot be null")
	private String name;

	@Email(message = "Invalid Email")
	private String email;

}
