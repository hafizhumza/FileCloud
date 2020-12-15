
package com.filecloud.uiservice.exception;

import com.filecloud.uiservice.response.Response.Status;

public class InvalidInputException extends ResponseException {

    private static final long serialVersionUID = 1L;

    public InvalidInputException() {
        this(Status.INVALID_INPUT.getMessage());
    }

    public InvalidInputException(String message) {
        super(Status.INVALID_INPUT, message);
    }
}
