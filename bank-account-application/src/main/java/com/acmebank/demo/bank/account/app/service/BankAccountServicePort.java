package com.acmebank.demo.bank.account.app.service;

import org.javamoney.moneta.Money;

import com.acmebank.demo.bank.account.app.domain.entity.BankAccount;
import com.acmebank.demo.bank.account.app.domain.exception.InsufficientBalanceException;
import com.acmebank.demo.bank.account.app.domain.vo.AccountNumber;

public interface BankAccountServicePort {

	/**
	 * Create a new Bank account
	 *
	 * @param lowerLimit
	 *            minimum balance autorized
	 */
	BankAccount createAccount(Money lowerLimit);

	/**
	 * Get Balance money operation.
	 *
	 * @param iban
	 *            Bank account identifier
	 * @return Balance amount
	 */
	Money getBalance(AccountNumber iban);

	/**
	 * Withdraw money operation.
	 *
	 * @param iban
	 *            Bank account identifier
	 * @param amount
	 *            Money amount to withdraw
	 * @throws InsufficientBalanceException
	 */
	void withdraw(AccountNumber iban, Money amount) throws InsufficientBalanceException;

	/**
	 * Deposit money operation.
	 *
	 * @param iban
	 *            Bank account identifier
	 * @param amount
	 *            money amount to deposit
	 */
	void deposit(AccountNumber iban, Money amount);

	/**
	 * Transfer money operation.
	 *
	 * @param fromIban
	 *            Source Bank account identifier
	 * @param toIban
	 *            target Bank account identifier
	 * @param amount
	 *            Money amount to transfer
	 * @throws InsufficientBalanceException
	 */
	void transfer(AccountNumber fromIban, AccountNumber toIban, Money amount) throws InsufficientBalanceException;

	/**
	 * Close the account according to the given Bank account identifier.
	 *
	 * @param iban
	 *            Bank account identifier
	 */
	void close(AccountNumber iban);

	/**
	 * Suspend the account according to the given Bank account identifier.
	 *
	 * @param iban
	 *            Bank account identifier
	 */
	void suspend(AccountNumber iban);

	/**
	 * Find BankAccount using his surrogate key.
	 *
	 * @param id
	 *            BankAccount surrogate key.
	 * @return BankAccount
	 */
	BankAccount findOne(Long id);

}