
package com.filecloud.authserver.exception;

import com.filecloud.authserver.response.Response.Status;

public class InvalidInputException extends ResponseException {

    private static final long serialVersionUID = 1L;

    public InvalidInputException() {
        this(Status.INVALID_INPUT.getMessage());
    }

    public InvalidInputException(String message) {
        super(Status.INVALID_INPUT, message);
    }
}
