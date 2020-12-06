
package com.filecloud.emailservice.controller;


import com.filecloud.emailservice.response.ErrorResult;
import com.filecloud.emailservice.response.Response.Status;
import com.filecloud.emailservice.response.Result;
import com.filecloud.emailservice.response.SuccessResult;

public class BaseController {

    /**
     * Success Result
     */

    public static <T> Result sendSuccessResponse(Status status) {
        return sendSuccessResponse(status.getStatusCode(), status.getMessage(), null);
    }

    public static <T> Result sendSuccessResponse(Status status, String message) {
        return sendSuccessResponse(status.getStatusCode(), message, null);
    }

    public static <T> Result sendSuccessResponse(Status status, T body) {
        return sendSuccessResponse(status.getStatusCode(), status.getMessage(), body);
    }

    public static <T> Result sendSuccessResponse(Status status, String message, T body) {
        return sendSuccessResponse(status.getStatusCode(), message, body);
    }

    /**
     * Error Result
     */

    public static <T> Result sendErrorResponse(Status status) {
        return sendErrorResponse(status.getStatusCode(), status.getMessage(), null);
    }

    public static <T> Result sendErrorResponse(Status status, String message) {
        return sendErrorResponse(status.getStatusCode(), message, null);
    }

    public static <T> Result sendErrorResponse(Status status, T errors) {
        return sendErrorResponse(status, status.getMessage(), errors);
    }

    public static <T> Result sendErrorResponse(Status status, String message, T errors) {
        return sendErrorResponse(status.getStatusCode(), message, errors);
    }

    /**
     * Generic Methods
     */

    public static <T> Result sendSuccessResponse(int status, String message, T body) {
        SuccessResult<T> result = new SuccessResult<T>(status);
        result.setMessage(message);
        result.setBody(body);
        return result;
    }

    public static <T> Result sendErrorResponse(int status, String message, T errors) {
        ErrorResult<T> result = new ErrorResult<T>(status);
        result.setMessage(message);
        result.setErrors(errors);
        return result;
    }
}
