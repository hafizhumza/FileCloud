
package com.filecloud.emailservice.response;

public class ErrorResult<T> extends Result {

	private T errors;

	public ErrorResult(int status) {
		super(status, false);
	}

	public ErrorResult(int status, T errors) {
		this(status);
		this.errors = errors;
	}

	public ErrorResult(int status, String message, T errors) {
		super(status, false, message);
		this.errors = errors;
	}

	public T getErrors() {
		return errors;
	}

	public void setErrors(T errors) {
		this.errors = errors;
	}
}
