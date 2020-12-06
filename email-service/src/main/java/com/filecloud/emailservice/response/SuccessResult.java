
package com.filecloud.emailservice.response;

public class SuccessResult<T> extends Result {

	private T body;

	public SuccessResult(int status) {
		super(status, true);
	}

	public SuccessResult(int status, T body) {
		this(status);
		this.body = body;
	}

	public T getBody() {
		return body;
	}

	public void setBody(T body) {
		this.body = body;
	}
}
