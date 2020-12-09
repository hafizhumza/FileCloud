package com.filecloud.documentservice.util;

import com.filecloud.documentservice.model.db.Document;
import org.springframework.http.HttpHeaders;

public class HeaderUtil {

    public static HttpHeaders getDocumentHeaders(Document document) {
        String fileName = document.getDisplayName();

        if (Util.isValidString(document.getExtension()))
            fileName = fileName.concat(".").concat(document.getExtension());

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");
        return header;
    }
}
