package com.acmebank.demo.bank.account.app.domain.exception;

public class InsufficientBalanceException extends RuntimeException {

    private static final long serialVersionUID = -6954419602515651819L;

    public InsufficientBalanceException(final String message) {
        super(message);
    }

    public InsufficientBalanceException() {
        super();
    }

}
