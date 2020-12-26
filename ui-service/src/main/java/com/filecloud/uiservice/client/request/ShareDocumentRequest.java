package com.filecloud.uiservice.client.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShareDocumentRequest {

    @NotNull(message = "Document ID cannot be null")
    @Min(value = 1, message = "Invalid document ID")
    private Long documentId;

    @NotBlank(message = "Email cannot be empty")
    @NotNull(message = "Email cannot be null")
    private String receiverEmail;

}
