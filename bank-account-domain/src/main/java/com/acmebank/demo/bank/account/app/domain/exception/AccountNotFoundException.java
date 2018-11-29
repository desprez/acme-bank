package com.acmebank.demo.bank.account.app.domain.exception;

public class AccountNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -8321303058408129338L;

	public AccountNotFoundException(final String message) {
		super(message);
	}

	public AccountNotFoundException() {
		super();
	}
}
