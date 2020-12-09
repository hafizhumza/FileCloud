package com.filecloud.documentservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DownloadDocumentDto {

    private ByteArrayResource resource;

    private HttpHeaders headers;

}
