package com.filecloud.uiservice.dto.mvcmodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentUploadModel {

    private MultipartFile document;

    @NotEmpty(message = "Name cannot be null")
    @NotNull(message = "Name cannot be null")
    private String name;

    private String description;

}
