package com.filecloud.uiservice.client.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentResponse {

    private Long documentId;
    private String displayName;
    private String description;
    private String extension;
    private Long sizeInBytes;
    private Long createDate;
    private Long modifyDate;

}
