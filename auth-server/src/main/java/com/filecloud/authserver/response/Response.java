
package com.filecloud.authserver.response;

public class Response {

	public static enum Status {

		/**
		 * Successes
		 */
		ALL_OK(200, "Request Completed Successfully"),
		LOGIN_SUCCESSFUL(200, "Login Successful"),
		CONNECTION_SUCCESSFUL(200, "Connection Successful"),
		USER_CREATED(200, "New User Created"),

		/**
		 * Errors
		 */
		CONNECTION_FAILED(400, "Connection Failed"),
		RECORD_NOT_FOUND(404, "Record not found."),
		USER_NOT_FOUND(404, "User Not Found"),

		/**
		 * Exceptions + Errors
		 */

		INTERNAL_SERVER_ERROR(500, "An error occurred on server side"),
		INVALID_ACCESS(403, "Access with the provided credentials denied."),
		DUPLICATE_EMAIL(407, "Email already exists"),
		INVALID_INPUT(511, "Invalid Input"),
		BAD_REQUEST(400, "Invalid request data on the client side");

		private int statusCode;
		private String message;

		Status(int code, String message) {
			this.statusCode = code;
			this.message = message;
		}

		public int getStatusCode() {
			return statusCode;
		}

		public void setStatusCode(int code) {
			this.statusCode = code;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
	}
}
