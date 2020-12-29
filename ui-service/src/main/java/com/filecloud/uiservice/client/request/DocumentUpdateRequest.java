package com.filecloud.uiservice.client.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentUpdateRequest {

    @NotNull(message = "Document ID cannot be null")
    @Min(value = 1, message = "Invalid document ID")
    private Long documentId;

    @NotBlank(message = "Document name cannot be empty")
    @NotNull(message = "Document name cannot be null")
    private String name;

    private String description;

}
