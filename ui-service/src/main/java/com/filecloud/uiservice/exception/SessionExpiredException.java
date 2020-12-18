
package com.filecloud.uiservice.exception;

import com.filecloud.uiservice.response.Response.Status;

public class SessionExpiredException extends ResponseException {

    private static final long serialVersionUID = 1L;

    public SessionExpiredException() {
        this(Status.SESSION_EXPIRED_EXCEPTION.getMessage());
    }

    public SessionExpiredException(String message) {
        super(Status.SESSION_EXPIRED_EXCEPTION, message);
    }
}
