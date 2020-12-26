
package com.filecloud.authserver.exception;

import com.filecloud.authserver.response.Response.Status;


public class DuplicateEmailException extends ResponseException {

	private static final long serialVersionUID = 1L;

	public DuplicateEmailException() {
		this(Status.DUPLICATE_EMAIL.getMessage());
	}

	public DuplicateEmailException(String message) {
		super(Status.DUPLICATE_EMAIL, message);
	}
}
