
package com.filecloud.documentservice.exception;

import com.filecloud.documentservice.response.Response.Status;

public class InvalidAccessException extends ResponseException {

    private static final long serialVersionUID = 1L;

    public InvalidAccessException() {
        this(Status.INVALID_ACCESS.getMessage());
    }

    public InvalidAccessException(String message) {
        super(Status.INVALID_ACCESS, message);
    }
}
