
package com.filecloud.uiservice.exception.handler;

import com.filecloud.uiservice.controller.BaseController;
import com.filecloud.uiservice.exception.HttpResponseException;
import com.filecloud.uiservice.exception.ResponseException;
import com.filecloud.uiservice.exception.WrappedException;
import com.filecloud.uiservice.properties.UiServiceProperties;
import com.filecloud.uiservice.response.Result;
import com.filecloud.uiservice.response.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class UiServiceExceptionHandler extends ResponseEntityExceptionHandler {

    private final UiServiceProperties uiServiceProperties;

    @Autowired
    public UiServiceExceptionHandler(UiServiceProperties uiServiceProperties) {
        this.uiServiceProperties = uiServiceProperties;
    }

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    Result<?> handleControllerException(HttpServletRequest req, Throwable ex) {
        return this.getExceptionResponse(ex);
    }

    @ExceptionHandler(ResponseException.class)
    @ResponseBody
    Result<?> handleResponseException(HttpServletRequest req, ResponseException ex) {

        if (ex.getStatus() == Status.INTERNAL_SERVER_ERROR)
            this.handleControllerException(req, ex);

        return BaseController.sendErrorResponse(ex.getStatus(), ex.getMessage());
    }

    @ExceptionHandler(HttpResponseException.class)
    @ResponseBody
    Result<?> handleHttpResponseException(HttpServletRequest req, HttpResponseException ex) {
        return BaseController.sendErrorResponse(ex.getStatusCode(), ex.getMessage(), null);
    }

    @ExceptionHandler(WrappedException.class)
    @ResponseBody
    Result<?> handleWrappedException(HttpServletRequest req, WrappedException ex) {

        if (ex.getActualException() instanceof ResponseException)
            return this.handleResponseException(req, (ResponseException) ex.getActualException());

        return this.handleControllerException(req, ex.getActualException());
    }

    private Result<?> getExceptionResponse(Throwable ex) {
        return uiServiceProperties.isDevMode() ? BaseController.sendErrorResponse(Status.INTERNAL_SERVER_ERROR, ex.getMessage()) : BaseController.sendErrorResponse(Status.INTERNAL_SERVER_ERROR, "Service failed.");
    }
}
