
package com.filecloud.emailservice.response;

public abstract class Result {

	private int status;

	private boolean success;

	private String message;

	Result(int status, boolean successfull) {
		this.status = status;
		this.success = successfull;
	}

	Result(int status, boolean successfull, String message) {
		this(status, successfull);
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
