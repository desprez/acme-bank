package com.acmebank.demo.bank.account.app.domain.exception;

public class NotZeroBalanceException extends RuntimeException {

	private static final long serialVersionUID = -8407267565550793580L;

	public NotZeroBalanceException(final String message) {
		super(message);
	}

	public NotZeroBalanceException() {
		super();
	}
}
