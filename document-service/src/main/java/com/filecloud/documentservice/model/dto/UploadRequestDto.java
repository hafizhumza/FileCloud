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

}
