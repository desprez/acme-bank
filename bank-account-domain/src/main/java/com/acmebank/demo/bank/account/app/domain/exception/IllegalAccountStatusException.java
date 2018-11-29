package com.acmebank.demo.bank.account.app.domain.exception;

public class IllegalAccountStatusException extends RuntimeException {

	private static final long serialVersionUID = -5580602390032282239L;

	public IllegalAccountStatusException(final String message) {
		super(message);
	}

	public IllegalAccountStatusException() {
		super();
	}
}
