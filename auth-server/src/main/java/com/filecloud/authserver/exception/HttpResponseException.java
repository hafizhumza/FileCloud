
package com.filecloud.authserver.exception;

import com.filecloud.authserver.response.Response.Status;


public class HttpResponseException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private int statusCode;

	public HttpResponseException() {
		super(Status.INTERNAL_SERVER_ERROR.getMessage());
		this.statusCode = Status.INTERNAL_SERVER_ERROR.getStatusCode();
	}

	public HttpResponseException(int statusCode) {
		this.statusCode = statusCode;
	}

	public HttpResponseException(int statusCode, String message) {
		super(message);
		this.statusCode = statusCode;
	}

	public HttpResponseException(String message) {
		super(message);
		this.statusCode = Status.INTERNAL_SERVER_ERROR.getStatusCode();
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
}
