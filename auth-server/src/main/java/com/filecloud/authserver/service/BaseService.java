
package com.filecloud.authserver.service;

import com.filecloud.authserver.exception.BadRequestException;
import com.filecloud.authserver.exception.DuplicateEmailException;
import com.filecloud.authserver.exception.HttpResponseException;
import com.filecloud.authserver.exception.InvalidAccessException;
import com.filecloud.authserver.exception.InvalidInputException;
import com.filecloud.authserver.exception.RecordNotFoundException;
import com.filecloud.authserver.exception.ResponseException;
import com.filecloud.authserver.exception.WrappedException;
import com.filecloud.authserver.response.Response.Status;


public class BaseService {

	public static void error() {
		throw new ResponseException();
	}

	public static void error(Throwable ex) {
		throw new WrappedException(ex);
	}

	public static void error(String message) {
		throw new ResponseException(message);
	}

	public static void error(Status status) {
		throw new ResponseException(status);
	}

	public static void error(Status status, String message) {
		throw new ResponseException(status, message);
	}

	public static void error(int statusCode) {
		throw new HttpResponseException(statusCode);
	}

	public static void error(int statusCode, String message) {
		throw new HttpResponseException(statusCode, message);
	}

	public static void notFound() {
		throw new RecordNotFoundException();
	}

	public static void notFound(String message) {
		throw new RecordNotFoundException(message);
	}

	public static void invalidInput() {
		throw new InvalidInputException();
	}

	public static void invalidInput(String message) {
		throw new InvalidInputException(message);
	}

	public static void badRequest() {
		throw new BadRequestException();
	}

	public static void badRequest(String message) {
		throw new BadRequestException(message);
	}

	public static void invalidAccess() {
		throw new InvalidAccessException();
	}

	public static void invalidAccess(String message) {
		throw new InvalidAccessException(message);
	}

	public static void duplicateEmail() {
		throw new DuplicateEmailException();
	}

	public static void duplicateEmail(String message) {
		throw new DuplicateEmailException(message);
	}

}
