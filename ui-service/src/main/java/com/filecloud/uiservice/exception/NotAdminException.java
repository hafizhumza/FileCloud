
package com.filecloud.uiservice.exception;

import com.filecloud.uiservice.response.Response.Status;

public class NotAdminException extends ResponseException {

    private static final long serialVersionUID = 1L;

    public NotAdminException() {
        this(Status.NOT_ADMIN_EXCEPTION.getMessage());
    }

    public NotAdminException(String message) {
        super(Status.NOT_ADMIN_EXCEPTION, message);
    }
}
