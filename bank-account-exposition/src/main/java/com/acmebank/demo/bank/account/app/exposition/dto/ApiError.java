package com.acmebank.demo.bank.account.app.exposition.dto;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;

/**
 *
 */
public class ApiError {

	private final HttpStatus status;
	private final String message;
	private final List<String> errors;

	public ApiError(final HttpStatus status, final String message, final List<String> errors) {
		super();
		this.status = status;
		this.message = message;
		this.errors = errors;
	}

	public ApiError(final HttpStatus status, final String message, final String error) {
		super();
		this.status = status;
		this.message = message;
		errors = Arrays.asList(error);
	}

	public HttpStatus getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

	public List<String> getErrors() {
		return errors;
	}

}