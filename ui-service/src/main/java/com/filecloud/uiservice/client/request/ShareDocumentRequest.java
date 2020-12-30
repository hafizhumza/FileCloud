package com.filecloud.uiservice.client.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShareDocumentRequest {

    @NotNull(message = "Document ID cannot be null")
    @Min(value = 1, message = "Invalid document ID")
    private Long documentId;

    @Email(message = "Invalid email")
    private String receiverEmail;

}
