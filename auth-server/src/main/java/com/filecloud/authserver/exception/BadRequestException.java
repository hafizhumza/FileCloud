
package com.filecloud.authserver.exception;

import com.filecloud.authserver.response.Response.Status;

public class BadRequestException extends ResponseException {

    private static final long serialVersionUID = 1L;

    public BadRequestException() {
        this(Status.BAD_REQUEST.getMessage());
    }

    public BadRequestException(String message) {
        super(Status.BAD_REQUEST, message);
    }
}
