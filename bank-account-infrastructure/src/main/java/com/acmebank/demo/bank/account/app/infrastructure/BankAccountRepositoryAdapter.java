package com.acmebank.demo.bank.account.app.infrastructure;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.acmebank.demo.bank.account.app.domain.entity.BankAccount;
import com.acmebank.demo.bank.account.app.domain.repository.BankAccountRepositoryPort;
import com.acmebank.demo.bank.account.app.domain.vo.AccountNumber;

@Component
public class BankAccountRepositoryAdapter implements BankAccountRepositoryPort {

	private final BankAccountJpaRepository bankAccountJpaRepository;

	/**
	 * Constructor for dependency injection
	 *
	 * @param bankAccountJpaRepository
	 */
	public BankAccountRepositoryAdapter(final BankAccountJpaRepository bankAccountJpaRepository) {
		this.bankAccountJpaRepository = bankAccountJpaRepository;
	}

	@Override
	public Optional<BankAccount> findByAccountNumber(final AccountNumber accountNumber) {
		return bankAccountJpaRepository.findByAccountNumber(accountNumber);
	}

	@Override
	public BankAccount save(final BankAccount account) {
		return bankAccountJpaRepository.save(account);
	}

	@Override
	public BankAccount findOne(final Long id) {
		return bankAccountJpaRepository.getOne(id);
	}

}
