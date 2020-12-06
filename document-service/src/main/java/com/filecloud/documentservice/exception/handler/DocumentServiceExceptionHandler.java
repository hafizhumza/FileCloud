
package com.filecloud.documentservice.exception.handler;

import com.filecloud.documentservice.controller.BaseController;
import com.filecloud.documentservice.exception.HttpResponseException;
import com.filecloud.documentservice.exception.ResponseException;
import com.filecloud.documentservice.exception.WrappedException;
import com.filecloud.documentservice.properties.DocumentServiceProperties;
import com.filecloud.documentservice.response.Response.Status;
import com.filecloud.documentservice.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@EnableWebMvc
@ControllerAdvice
public class DocumentServiceExceptionHandler extends ResponseEntityExceptionHandler {

    private final DocumentServiceProperties documentServiceProperties;

    @Autowired
    public DocumentServiceExceptionHandler(DocumentServiceProperties documentServiceProperties) {
        this.documentServiceProperties = documentServiceProperties;
    }

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    Result handleControllerException(HttpServletRequest req, Throwable ex) {
        return this.getExceptionResponse(ex);
    }

    @ExceptionHandler(ResponseException.class)
    @ResponseBody
    Result handleResponseException(HttpServletRequest req, ResponseException ex) {

        if (ex.getStatus() == Status.INTERNAL_SERVER_ERROR)
            this.handleControllerException(req, ex);

        return BaseController.sendErrorResponse(ex.getStatus(), ex.getMessage());
    }

    @ExceptionHandler(HttpResponseException.class)
    @ResponseBody
    Result handleHttpResponseException(HttpServletRequest req, HttpResponseException ex) {
        return BaseController.sendErrorResponse(ex.getStatusCode(), ex.getMessage(), null);
    }

    @ExceptionHandler(WrappedException.class)
    @ResponseBody
    Result handleWrappedException(HttpServletRequest req, WrappedException ex) {

        if (ex.getActualException() instanceof ResponseException)
            return this.handleResponseException(req, (ResponseException) ex.getActualException());

        return this.handleControllerException(req, ex.getActualException());
    }

    private Result getExceptionResponse(Throwable ex) {
        return documentServiceProperties.isDevMode() ? BaseController.sendErrorResponse(Status.INTERNAL_SERVER_ERROR, ex.getMessage()) : BaseController.sendErrorResponse(Status.INTERNAL_SERVER_ERROR, "Service failed.");
    }
}
