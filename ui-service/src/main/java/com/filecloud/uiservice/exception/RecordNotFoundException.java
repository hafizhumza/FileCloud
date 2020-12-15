
package com.filecloud.uiservice.exception;

import com.filecloud.uiservice.response.Response.Status;

public class RecordNotFoundException extends ResponseException {

    private static final long serialVersionUID = 1L;

    public RecordNotFoundException() {
        this(Status.RECORD_NOT_FOUND.getMessage());
    }

    public RecordNotFoundException(String message) {
        super(Status.RECORD_NOT_FOUND, message);
    }
}
