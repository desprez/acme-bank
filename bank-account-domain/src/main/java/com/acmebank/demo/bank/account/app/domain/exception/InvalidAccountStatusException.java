package com.acmebank.demo.bank.account.app.domain.exception;

public class InvalidAccountStatusException extends RuntimeException {

	private static final long serialVersionUID = 3785250769222204973L;

	public InvalidAccountStatusException(final String message) {
		super(message);
	}

	public InvalidAccountStatusException() {
		super();
	}
}
