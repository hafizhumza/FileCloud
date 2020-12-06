package com.filecloud.documentservice.model.dto;

import com.filecloud.documentservice.model.db.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentResponseDto implements Serializable {

    private Long documentId;
    private String displayName;
    private String description;
    private String documentType;
    private String extension;
    private Double sizeInMb;
    private Long createDate;
    private Long modifyDate;

    public DocumentResponseDto(Document document) {
        documentId = document.getDocumentId();
        displayName = document.getDisplayName();
        description = document.getDescription();
        documentType = document.getDocumentType();
        extension = document.getExtension();
        sizeInMb = document.getSizeInMb();
        createDate = document.getCreateDate();
        modifyDate = document.getModifyDate();
    }

}
