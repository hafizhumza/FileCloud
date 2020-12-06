
package com.filecloud.emailservice.exception;

public class WrappedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private Throwable actualException;

	public WrappedException(Throwable exception) {
		this.actualException = exception;
	}

	public Throwable getActualException() {
		return actualException;
	}

	public void setActualException(Throwable actualException) {
		this.actualException = actualException;
	}
}
