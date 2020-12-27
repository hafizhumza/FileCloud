package com.filecloud.uiservice.dto.mvcmodel;

import com.filecloud.uiservice.client.response.DocumentResponse;
import com.filecloud.uiservice.util.Util;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentModel {

    private long id;
    private String name;
    private String description;
    private String extension;
    private String size;
    private String createDate;
    private String modifyDate;

    public DocumentModel(DocumentResponse response) {
        id = response.getDocumentId();
        name = response.getDisplayName();
        description = response.getDescription();
        extension = response.getExtension();
        size = Util.humanReadableByteCountBin(response.getSizeInBytes());
        createDate = new SimpleDateFormat("yyyy-MM-dd h:mm a").format(new Date(response.getCreateDate()));
        if (response.getModifyDate() != null && response.getModifyDate() > 0)
            modifyDate = new SimpleDateFormat("yyyy-MM-dd h:mm a").format(new Date(response.getModifyDate()));
    }

}
