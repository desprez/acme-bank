package com.acmebank.demo.bank.account.app.steps;

import static javax.money.Monetary.getCurrency;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;

import org.javamoney.moneta.Money;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import com.acmebank.demo.bank.account.app.domain.entity.BankAccount;
import com.acmebank.demo.bank.account.app.domain.exception.InsufficientBalanceException;
import com.acmebank.demo.bank.account.app.domain.exception.InvalidAccountStatusException;
import com.acmebank.demo.bank.account.app.domain.exception.NotSupportedException;
import com.acmebank.demo.bank.account.app.domain.vo.AccountNumber;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * Step implementation of the Feature: Account operations
 */
public class BankAccountSteps {

	private BankAccount bankAccount;

	private Exception ex;

	@Rule
	public ExpectedException expectedEx = ExpectedException.none();

	@Given("^An account \"([^\"]*)\" with Currency is \"([^\"]*)\"$")
	public void an_account_with_Currency_is(final String accountNumber, final String currency) throws Throwable {
		bankAccount = new BankAccount.Builder().accountNumber(AccountNumber.of(accountNumber))
				.lowerLimit(Money.of(0, getCurrency(currency))).build();
	}

	@Given("^An account \"([^\"]*)\" with lower limit (\\d+) \"([^\"]*)\"$")
	public void an_account_with_lower_limit(final String accountNumber, final int lowerLimit, final String currency) throws Throwable {
		bankAccount = new BankAccount.Builder().accountNumber(AccountNumber.of(accountNumber))
				.lowerLimit(Money.of(lowerLimit, getCurrency(currency))).build();
	}

	@Given("^a balance of (\\d+)$")
	public void a_balance_of(final int balance) throws Throwable {
		bankAccount.credit(Money.of(balance, bankAccount.getAccountCurrency()));
	}

	@When("^I withdraw (\\d+) \"([^\"]*)\"$")
	public void i_withdraw_from_account(final int amount, final String currency) throws Throwable {
		try {
			bankAccount.debit(Money.of(amount, getCurrency(currency)));
		} catch (final Exception e) {
			ex = e;
		}
	}

	@When("^I deposit (\\d+) \"([^\"]*)\"$")
	public void i_deposit(final int amount, final String currency) throws Throwable {
		try {
			bankAccount.credit(Money.of(amount, getCurrency(currency)));
		} catch (final Exception e) {
			ex = e;
		}
	}

	@Then("^Account \"([^\"]*)\" has balance (-?\\d+)$")
	public void account_has_balance(final String accountNumber, final BigDecimal expectedBalance) throws Throwable {
		assertThat(bankAccount.getBalance(), equalTo(Money.of(expectedBalance, getCurrency("EUR"))));
	}

	@Then("^it Fails with InsufficientBalanceException$")
	public void it_Fails() throws Throwable {
		assertThat(ex, instanceOf(InsufficientBalanceException.class));
	}

	@Then("^it Fails with MonetaryException$")
	public void it_Fails_with_MonetaryException() throws Throwable {
		assertThat(ex, instanceOf(NotSupportedException.class));
	}

	@Given("^account is closed$")
	public void account_is_closed() throws Throwable {
		bankAccount.close();
	}

	@Given("^account is suspended$")
	public void account_is_suspended() throws Throwable {
		bankAccount.suspend();
	}

	@Then("^it Fails with InvalidAccountStatusException$")
	public void it_Fails_with_InvalidAccountStatusException() throws Throwable {
		assertThat(ex, instanceOf(InvalidAccountStatusException.class));
	}
}