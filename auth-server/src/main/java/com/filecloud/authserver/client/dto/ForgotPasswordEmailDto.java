package com.filecloud.authserver.client.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForgotPasswordEmailDto {

	@Email(message = "Invalid email")
	private String receiverEmail;

	private String name;

	@NotNull(message = "URL cannot be null")
	private String url;

	private long expiryDays;

}
