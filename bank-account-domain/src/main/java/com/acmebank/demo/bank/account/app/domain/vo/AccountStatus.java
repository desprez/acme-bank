package com.acmebank.demo.bank.account.app.domain.vo;

import com.acmebank.demo.bank.account.app.domain.exception.IllegalAccountStatusException;

/**
 * Account Status object.
 */
public enum AccountStatus {

	ACTIVE, SUSPENDED(ACTIVE), CLOSED(ACTIVE, SUSPENDED);

	private AccountStatus[] previousStates;

	private AccountStatus(final AccountStatus... state) {
		previousStates = state;
	}

	/**
	 * Transition validation.
	 *
	 * @param state
	 *            the new Account Status
	 * @return new state if transition is valid
	 * @throws IllegalStateException
	 *             if transition isnt valid
	 */
	public AccountStatus transition(final AccountStatus state) {
		for (final AccountStatus tmp : state.previousStates) {
			if (this == tmp) {
				return state;
			}
		}
		throw new IllegalAccountStatusException("Illegal transition: " + this + " cannot be changed to " + state);
	}

}
