
package com.filecloud.authserver.exception;

import com.filecloud.authserver.response.Response.Status;

public class RecordNotFoundException extends ResponseException {

    private static final long serialVersionUID = 1L;

    public RecordNotFoundException() {
        this(Status.RECORD_NOT_FOUND.getMessage());
    }

    public RecordNotFoundException(String message) {
        super(Status.RECORD_NOT_FOUND, message);
    }
}
