package com.filecloud.documentservice.model.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShareDocumentRequestDto {

	@NotNull(message = "Document ID cannot be null")
	@Min(value = 1, message = "Invalid document ID")
	private Long documentId;

	@NotBlank(message = "Email cannot be empty")
	@NotNull(message = "Email cannot be null")
	private String receiverEmail;

}
