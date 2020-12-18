
package com.filecloud.uiservice.service;


import com.filecloud.uiservice.exception.*;
import com.filecloud.uiservice.response.Response;
import com.filecloud.uiservice.response.Response.Status;
import com.filecloud.uiservice.response.Result;

public class BaseService {

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

    public static void notFound(String message) {
        throw new RecordNotFoundException(message);
    }

    public static void invalidInput() {
        throw new InvalidInputException();
    }

    public static void invalidInput(String message) {
        throw new InvalidInputException(message);
    }

    public static void invalidAccess() {
        throw new InvalidAccessException();
    }

    public static void invalidAccess(String message) {
        throw new InvalidAccessException(message);
    }

    public static void sessionExpired() {
        throw new SessionExpiredException();
    }

    public static void checkResult(Result<?> result) {
        if (!(result.isSuccess() && result.getStatusCode() == Response.Status.ALL_OK.getStatusCode()))
            error(result.getMessage());
    }

}
