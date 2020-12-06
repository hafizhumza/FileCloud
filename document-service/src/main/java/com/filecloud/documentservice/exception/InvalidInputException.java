
package com.filecloud.documentservice.exception;

import com.filecloud.documentservice.response.Response.Status;

public class InvalidInputException extends ResponseException {

    private static final long serialVersionUID = 1L;

    public InvalidInputException() {
        this(Status.INVALID_INPUT.getMessage());
    }

    public InvalidInputException(String message) {
        super(Status.INVALID_INPUT, message);
    }
}
