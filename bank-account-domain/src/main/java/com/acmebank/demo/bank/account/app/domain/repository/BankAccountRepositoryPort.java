package com.acmebank.demo.bank.account.app.domain.repository;

import java.util.Optional;

import com.acmebank.demo.bank.account.app.domain.entity.BankAccount;
import com.acmebank.demo.bank.account.app.domain.vo.AccountNumber;

/**
 * Port for BankAccount Repository
 */
public interface BankAccountRepositoryPort {

	public BankAccount save(BankAccount account);

	public Optional<BankAccount> findByAccountNumber(final AccountNumber accountNumber);

	public BankAccount findOne(Long id);

}
