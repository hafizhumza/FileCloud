package com.filecloud.documentservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadRequestDto {

    @NotBlank(message = "Document name cannot be empty")
    @NotNull(message = "Document name cannot be null")
    private String name;

    private String description;

    @NotBlank(message = "Document type cannot be empty")
    @NotNull(message = "Document type cannot be null")
    private String type;

    @NotBlank(message = "Document extension cannot be empty")
    @NotNull(message = "Document extension cannot be null")
    private String extension;

}
