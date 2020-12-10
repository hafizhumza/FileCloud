
package com.filecloud.authserver.exception.handler;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.filecloud.authserver.controller.BaseController;
import com.filecloud.authserver.exception.HttpResponseException;
import com.filecloud.authserver.exception.ResponseException;
import com.filecloud.authserver.exception.WrappedException;
import com.filecloud.authserver.properties.AuthServerProperties;
import com.filecloud.authserver.response.Response.Status;
import com.filecloud.authserver.response.Result;


@EnableWebMvc
@ControllerAdvice
public class AuthServerExceptionHandler extends ResponseEntityExceptionHandler {

	private final AuthServerProperties authServerProperties;

	@Autowired
	public AuthServerExceptionHandler(AuthServerProperties authServerProperties) {
		this.authServerProperties = authServerProperties;
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
		return authServerProperties.isDevMode() ? BaseController.sendErrorResponse(Status.INTERNAL_SERVER_ERROR, ex.getMessage()) : BaseController.sendErrorResponse(Status.INTERNAL_SERVER_ERROR, "Service failed.");
	}
}
