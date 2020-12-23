package com.filecloud.emailservice.model.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SharedDocumentEmailDto {

	@NotBlank(message = "Email cannot be empty")
	@NotNull(message = "Email cannot be null")
	private String receiverEmail;

	private String senderName;

	private String url;

	private long expiryDays;

}
