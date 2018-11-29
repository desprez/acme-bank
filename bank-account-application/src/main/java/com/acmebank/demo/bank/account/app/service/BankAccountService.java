package com.acmebank.demo.bank.account.app.service;

import java.util.Optional;

import org.javamoney.moneta.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.acmebank.demo.bank.account.app.domain.entity.BankAccount;
import com.acmebank.demo.bank.account.app.domain.exception.AccountNotFoundException;
import com.acmebank.demo.bank.account.app.domain.exception.InvalidAccountNumberException;
import com.acmebank.demo.bank.account.app.domain.repository.BankAccountRepositoryPort;
import com.acmebank.demo.bank.account.app.domain.vo.AccountNumber;

/**
 * Service orchestration for BankAccount operations.
 */
@Service
@Transactional
public class BankAccountService implements BankAccountServicePort {

	private static final Logger LOG = LoggerFactory.getLogger(BankAccountService.class);

	private final BankAccountRepositoryPort accountRepository;

	public BankAccountService(final BankAccountRepositoryPort accountRepository) {
		this.accountRepository = accountRepository;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.acmebank.demo.bank.account.app.BankAccountServicePort#createAccount(
	 * org.javamoney.moneta.Money)
	 */
	@Override
	public BankAccount createAccount(final Money lowerLimit) {
		LOG.debug("createAccount with lowerLimit {}", lowerLimit);
		final BankAccount account = new BankAccount.Builder().accountNumber(AccountNumber.generate())
				.lowerLimit(lowerLimit).build();

		accountRepository.save(account);

		return account;
	}

	/**
	 * Find Bank Account from repository
	 *
	 * @param iban
	 *            Bank account identifier
	 * @return the banc Acount if found else throw AccountNotFoundException
	 */
	private BankAccount findAccount(final AccountNumber iban) {
		final Optional<BankAccount> accountFound = accountRepository.findByAccountNumber(iban);

		return accountFound.orElseThrow(() -> new AccountNotFoundException("Account " + iban + " was not found"));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.acmebank.demo.bank.account.app.BankAccountServicePort#getBalance(com
	 * .acmebank.demo.bank.account.app.domain.vo.AccountNumber)
	 */
	@Override
	public Money getBalance(final AccountNumber iban) {
		LOG.debug("getBalance for accountNumber {}", iban);

		final BankAccount account = findAccount(iban);

		return account.getBalance();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.acmebank.demo.bank.account.app.BankAccountServicePort#withdraw(com.
	 * acmebank.demo.bank.account.app.domain.vo.AccountNumber,
	 * org.javamoney.moneta.Money)
	 */
	@Override
	public void withdraw(final AccountNumber iban, final Money amount) {
		LOG.debug("withdraw amount of {} for accountNumber {}", amount, iban);
		final BankAccount account = findAccount(iban);

		account.debit(amount);
		accountRepository.save(account);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.acmebank.demo.bank.account.app.BankAccountServicePort#deposit(com.
	 * acmebank.demo.bank.account.app.domain.vo.AccountNumber,
	 * org.javamoney.moneta.Money)
	 */
	@Override
	public void deposit(final AccountNumber iban, final Money amount) {
		LOG.debug("deposit amount of {} for accountNumber {}", amount, iban);
		// Find bank account by using iban
		final BankAccount account = findAccount(iban);

		account.credit(amount);
		accountRepository.save(account);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.acmebank.demo.bank.account.app.BankAccountServicePort#transfer(com.
	 * acmebank.demo.bank.account.app.domain.vo.AccountNumber,
	 * com.acmebank.demo.bank.account.app.domain.vo.AccountNumber,
	 * org.javamoney.moneta.Money)
	 */
	@Override
	public void transfer(final AccountNumber fromIban, final AccountNumber toIban, final Money amount) {

		LOG.debug("transfer amount of {} from accountNumber {} to {} ", amount, fromIban, toIban);
		final BankAccount fromAccount = findAccount(fromIban);
		final BankAccount toAccount = findAccount(toIban);
		if (fromAccount.equals(toAccount)) {
			throw new InvalidAccountNumberException("Transfert to account itself is not allowed");
		}
		fromAccount.transfer(toAccount, amount);

		accountRepository.save(fromAccount);
		accountRepository.save(toAccount);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.acmebank.demo.bank.account.app.BankAccountServicePort#close(com.
	 * acmebank.demo.bank.account.app.domain.vo.AccountNumber)
	 */
	@Override
	public void close(final AccountNumber iban) {
		LOG.debug("Close account for number {}", iban);
		// Find bank account by using iban
		final BankAccount account = findAccount(iban);

		account.close();
		accountRepository.save(account);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.acmebank.demo.bank.account.app.BankAccountServicePort#suspend(com.
	 * acmebank.demo.bank.account.app.domain.vo.AccountNumber)
	 */
	@Override
	public void suspend(final AccountNumber iban) {
		LOG.debug("Suspend account for number {}", iban);
		// Find bank account by using iban
		final BankAccount account = findAccount(iban);

		account.suspend();
		accountRepository.save(account);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.acmebank.demo.bank.account.app.BankAccountServicePort#findOne(java.
	 * lang.Long)
	 */
	@Override
	public BankAccount findOne(final Long id) {
		return accountRepository.findOne(id);
	}

}