
package com.filecloud.emailservice.exception;

import com.filecloud.emailservice.response.Response.Status;

public class ResponseException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private Status status;

    public ResponseException() {
        super(Status.INTERNAL_SERVER_ERROR.getMessage());
        this.status = Status.INTERNAL_SERVER_ERROR;
    }

    public ResponseException(Status status) {
        super(status.getMessage());
        this.status = status;
    }

    public ResponseException(Status status, String message) {
        super(message);
        this.status = status;
    }

    public ResponseException(String message) {
        super(message);
        this.status = Status.INTERNAL_SERVER_ERROR;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
