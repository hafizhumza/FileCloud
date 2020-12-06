package com.filecloud.documentservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailSharedDocumentUrlDto {

    @NotBlank(message = "Email cannot be empty")
    @NotNull(message = "Email cannot be null")
    private String toEmail;

    private String fromName;

    private String url;

    private int expiryDays;

}
