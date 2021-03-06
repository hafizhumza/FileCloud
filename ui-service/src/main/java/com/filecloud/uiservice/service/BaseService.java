
package com.filecloud.uiservice.service;


import com.filecloud.uiservice.exception.*;
import com.filecloud.uiservice.response.Response;
import com.filecloud.uiservice.response.Response.Status;
import com.filecloud.uiservice.response.Result;

import java.util.logging.Logger;

public class BaseService {

    private static final Logger logger = Logger.getLogger(BaseService.class.getSimpleName());

    public static void error() {
        throw new ResponseException();
    }

    public static void error(String message) {
        throw new ResponseException(message);
    }

    public static void error(Status status) {
        throw new ResponseException(status);
    }

    public static void error(Status status, String message) {
        throw new ResponseException(status, message);
    }

    public static void notFound() {
        throw new RecordNotFoundException();
    }

    public static void invalidInput() {
        throw new InvalidInputException();
    }

    public static void invalidAccess() {
        throw new InvalidAccessException();
    }

    public static void sessionExpired() {
        throw new SessionExpiredException();
    }

    public static void logIfError(Result<?> result) {
        if (!(result.isSuccess() && result.getStatusCode() == Response.Status.ALL_OK.getStatusCode()))
            logger.severe(String.format("Service failed with status code %s. Message: %s", result.getStatusCode(), result.getMessage()));
    }

    public static void throwIfInvalidAccess(Result<?> result) {
        if (result.getStatusCode() == Status.INVALID_ACCESS.getStatusCode())
            invalidAccess();
    }

//    public static void throwIfInvalidAccessOrInternalError(Result<?> result) {
//        if (result.getStatusCode() == Status.INVALID_ACCESS.getStatusCode())
//            invalidAccess();
//
//        throwIfInternalServerError(result);
//    }

    public static void throwIfInternalServerError(Result<?> result) {
        if (result.getStatusCode() == Status.INTERNAL_SERVER_ERROR.getStatusCode())
            error();
    }
}
