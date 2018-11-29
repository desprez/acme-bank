package com.acmebank.demo.bank.account.app.domain.entity;

import java.time.LocalDate;

import javax.money.CurrencyUnit;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.javamoney.moneta.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acmebank.demo.bank.account.app.domain.exception.InsufficientBalanceException;
import com.acmebank.demo.bank.account.app.domain.exception.InvalidAccountStatusException;
import com.acmebank.demo.bank.account.app.domain.exception.NotSupportedException;
import com.acmebank.demo.bank.account.app.domain.exception.NotZeroBalanceException;
import com.acmebank.demo.bank.account.app.domain.vo.AccountNumber;
import com.acmebank.demo.bank.account.app.domain.vo.AccountStatus;

/**
 * Class of bank accounts (Entity).
 */
public class BankAccount extends BaseEntity {

	private static final Logger LOG = LoggerFactory.getLogger(BankAccount.class);

	public BankAccount() {
		super();
	}

	private AccountNumber accountNumber; // functional identifier kwown by external provider

	private Money balance; // The balance of this account

	private Money lowerLimit; // The minimum balance

	private History history; // The history list of this account

	private AccountStatus status;

	private LocalDate createdDate;

	/**
	 * The balance of this account.
	 */
	public Money getBalance() {
		return balance;
	}

	/**
	 * The history list of this account.
	 */
	public History getHistory() {
		return history;
	}

	/**
	 * The status of this account.
	 */
	public AccountStatus getStatus() {
		return status;
	}

	/**
	 * Create Date of this account.
	 */
	public LocalDate getCreatedDate() {
		return createdDate;
	}

	/**
	 * The minimum balance of this account.
	 */
	public Money getLowerLimit() {
		return lowerLimit;
	}

	/**
	 * International Bank Account Number
	 */
	public AccountNumber getAccountNumber() {
		return accountNumber;
	}

	public CurrencyUnit getAccountCurrency() {
		return lowerLimit.getCurrency();
	}

	/**
	 * Credits this account with the specified amount.
	 *
	 * @param amount
	 *            amount to credit
	 */
	public void credit(final Money amount) {
		LOG.debug("Account {}, Credit operation : {}", accountNumber, amount);
		if (isClosed()) {
			throw new InvalidAccountStatusException("This accound is close");
		}
		final Money accountCurrencyAmount = convertIfNecessary(amount);
		history = new History(balance, getHistory(), LocalDate.now());
		balance = balance.add(accountCurrencyAmount);
	}

	/**
	 * Method used to substract the given amount from account balance
	 *
	 * @param amount
	 *            amount to debit
	 * @throws InsufficientBalanceException
	 *             if amout is greater than balance
	 */
	public void debit(final Money amount) throws InsufficientBalanceException {
		LOG.debug("Account {}, Debit operation : {}", accountNumber, amount);
		if (!isActive()) {
			throw new InvalidAccountStatusException("This accound is not active");
		}
		final Money accountCurrencyAmount = convertIfNecessary(amount);
		if (accountCurrencyAmount.isGreaterThan(balance.add(lowerLimit))) {
			throw new InsufficientBalanceException("Insufficient total balance");
		} else {
			history = new History(balance, getHistory(), LocalDate.now());
			balance = balance.subtract(accountCurrencyAmount);
		}
	}

	/**
	 *
	 * @param amount
	 * @return
	 */
	public Money convertIfNecessary(final Money amount) {
		if (!getAccountCurrency().equals(amount.getCurrency())) {
			throw new NotSupportedException(
					String.format("Currency %s not match account currency %s and conversion is not Supported yet",
							amount.getCurrency(), getAccountCurrency()));
		}
		return amount;
	}

	/**
	 * Method used to transfer money amount from this account to the given amount
	 *
	 * @param targetAccount
	 *            account to be credited
	 * @param amount
	 *            amount to transfer
	 */
	public void transfer(final BankAccount targetAccount, final Money amount) {
		LOG.debug("Transfert {} from {} to {}", amount, accountNumber, targetAccount);
		debit(amount);
		targetAccount.credit(amount);
	}

	/**
	 * Cancels the last credit or debit operation.
	 */
	public void cancel() {
		balance = history.getBalance();
		history = history.getPrevious();
	}

	public boolean isActive() {
		return AccountStatus.ACTIVE.equals(status);
	}

	public void close() {
		if (!getBalance().isZero()) {
			throw new NotZeroBalanceException("Closing account expect zero balance");
		}
		status = status.transition(AccountStatus.CLOSED);
	}

	public boolean isClosed() {
		return AccountStatus.CLOSED.equals(status);
	}

	public void suspend() {
		status = status.transition(AccountStatus.SUSPENDED);
	}

	public boolean isSuspended() {
		return AccountStatus.SUSPENDED.equals(status);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (accountNumber == null ? 0 : accountNumber.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final BankAccount other = (BankAccount) obj;
		return new EqualsBuilder().append(accountNumber, other.accountNumber).isEquals();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append(getId()).append(accountNumber)
				.append(balance).append(lowerLimit).append(history).append(status).append(createdDate).toString();
	}

	/**
	 * Builder pattern
	 */
	public static class Builder {

		private AccountNumber accountNumber;

		private Money lowerLimit;

		/**
		 * Sets the minimum balance to the specified value.
		 */
		public Builder accountNumber(final AccountNumber accountNumber) {
			this.accountNumber = accountNumber;
			return this;
		}

		/**
		 * Sets the minimum balance to the specified value.
		 */
		public Builder lowerLimit(final Money lowerLimit) {
			this.lowerLimit = lowerLimit;
			return this;
		}

		public BankAccount build() {
			return new BankAccount(this);
		}
	}

	private BankAccount(final Builder builder) {
		accountNumber = builder.accountNumber;
		lowerLimit = builder.lowerLimit;
		balance = Money.of(0, getAccountCurrency());
		status = AccountStatus.ACTIVE;
		createdDate = LocalDate.now();
	}
}