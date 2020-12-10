
package com.filecloud.adminservice.exception.handler;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.filecloud.adminservice.controller.BaseController;
import com.filecloud.adminservice.exception.HttpResponseException;
import com.filecloud.adminservice.exception.ResponseException;
import com.filecloud.adminservice.exception.WrappedException;
import com.filecloud.adminservice.properties.AdminServiceProperties;
import com.filecloud.adminservice.response.Response.Status;
import com.filecloud.adminservice.response.Result;


@EnableWebMvc
@ControllerAdvice
public class AdminServiceExceptionHandler extends ResponseEntityExceptionHandler {

    private final AdminServiceProperties adminServiceProperties;

    @Autowired
    public AdminServiceExceptionHandler(AdminServiceProperties adminServiceProperties) {
        this.adminServiceProperties = adminServiceProperties;
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
        return adminServiceProperties.isDevMode() ? BaseController.sendErrorResponse(Status.INTERNAL_SERVER_ERROR, ex.getMessage()) : BaseController.sendErrorResponse(Status.INTERNAL_SERVER_ERROR, "Service failed.");
    }
}
