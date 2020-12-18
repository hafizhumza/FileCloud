
package com.filecloud.uiservice.exception;

import com.filecloud.uiservice.response.Response.Status;

public class NotUserException extends ResponseException {

    private static final long serialVersionUID = 1L;

    public NotUserException() {
        this(Status.NOT_USER_EXCEPTION.getMessage());
    }

    public NotUserException(String message) {
        super(Status.NOT_USER_EXCEPTION, message);
    }
}
