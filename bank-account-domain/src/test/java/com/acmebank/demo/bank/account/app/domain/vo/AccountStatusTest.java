package com.acmebank.demo.bank.account.app.domain.vo;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.acmebank.demo.bank.account.app.domain.exception.IllegalAccountStatusException;
import com.acmebank.demo.bank.account.app.domain.vo.AccountStatus;

public class AccountStatusTest {

	private AccountStatus status;

	@Rule
	public ExpectedException expectedEx = ExpectedException.none();

	@Test
	public void activeToSuspendedTransitionShouldSuccess() throws Exception {
		// Given
		status = AccountStatus.ACTIVE;
		// When
		status = status.transition(AccountStatus.SUSPENDED);
		// Then
		assertEquals(AccountStatus.SUSPENDED, status);
	}

	@Test(expected = IllegalAccountStatusException.class)
	public void activeToActiveTransitionShouldFail() throws Exception {
		// Given
		AccountStatus state = AccountStatus.ACTIVE;
		// When
		state = state.transition(AccountStatus.ACTIVE);
		// Then fails
	}

	@Test
	public void activeToClosedTransitionShouldSuccess() throws Exception {
		// Given
		status = AccountStatus.ACTIVE;
		// When
		status = status.transition(AccountStatus.CLOSED);
		// Then
		assertEquals(AccountStatus.CLOSED, status);
	}

	@Test(expected = IllegalAccountStatusException.class)
	public void closedToSuspendedTransitionShouldFail() throws Exception {
		// Given
		AccountStatus state = AccountStatus.CLOSED;
		// When
		state = state.transition(AccountStatus.SUSPENDED);
		// Then fails
	}

	@Test(expected = IllegalAccountStatusException.class)
	public void closedToActiveTransitionShouldFail() throws Exception {
		// Given
		AccountStatus state = AccountStatus.CLOSED;
		// When
		state = state.transition(AccountStatus.ACTIVE);
		// Then fails
	}

	@Test(expected = IllegalAccountStatusException.class)
	public void suspendedToActiveTransitionShouldFail() throws Exception {
		// Given
		AccountStatus state = AccountStatus.SUSPENDED;
		// When
		state = state.transition(AccountStatus.ACTIVE);
		// Then fails
	}

	@Test(expected = IllegalAccountStatusException.class)
	public void suspendedToSuspendedTransitionShouldFail() throws Exception {
		// Given
		AccountStatus state = AccountStatus.SUSPENDED;
		// When
		state = state.transition(AccountStatus.ACTIVE);
		// Then fails
	}
}