
package com.filecloud.uiservice.controller;

import com.filecloud.uiservice.response.Result;
import com.filecloud.uiservice.response.Response.Status;

public class BaseController {

    /**
     * Success Result
     */

    public static <T> Result<?> sendSuccessResponse(Status status) {
        return sendResponse(true, status.getStatusCode(), null, null, null);
    }

    public static <T> Result<?> sendSuccessResponse(Status status, String message) {
        return sendResponse(true, status.getStatusCode(), message, null, null);
    }

    public static <T> Result<?> sendSuccessResponse(Status status, T data) {
        return sendResponse(true, status.getStatusCode(), null, null, data);
    }

    public static <T> Result<?> sendSuccessResponse(Status status, String message, T data) {
        return sendResponse(true, status.getStatusCode(), message, null, data);
    }

    /**
     * Error Result
     */

    public static <T> Result<?> sendErrorResponse(Status status) {
        return sendResponse(false, status.getStatusCode(), status.getMessage(), null, null);
    }

    public static <T> Result<?> sendErrorResponse(Status status, String message) {
        return sendResponse(false, status.getStatusCode(), message, null, null);
    }

    public static <T> Result<?> sendErrorResponse(Status status, String message, String errorMessage) {
        return sendResponse(false, status.getStatusCode(), message, errorMessage, null);
    }

    public static <T> Result<?> sendErrorResponse(Status status, String message, String errorMessage, T data) {
        return sendResponse(false, status.getStatusCode(), message, errorMessage, data);
    }

    public static <T> Result<?> sendErrorResponse(int status, String message, String errorMessage) {
        return sendResponse(false, status, message, errorMessage, null);
    }

    public static <T> Result<?> sendErrorResponse(int status, String message, String errorMessage, T data) {
        return sendResponse(false, status, message, errorMessage, data);
    }

    /**
     * Generic Methods
     */

    public static <T> Result<?> sendResponse(boolean success, int status, String message, String errorMessage, T data) {
        Result<T> result = new Result<T>();
        result.setSuccess(success);
        result.setStatusCode(status);
        result.setMessage(message);
        result.setErrorMessage(errorMessage);
        result.setData(data);
        return result;
    }
}
