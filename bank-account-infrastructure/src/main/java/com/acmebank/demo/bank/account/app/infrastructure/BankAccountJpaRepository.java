package com.acmebank.demo.bank.account.app.infrastructure;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.acmebank.demo.bank.account.app.domain.entity.BankAccount;
import com.acmebank.demo.bank.account.app.domain.vo.AccountNumber;

/**
 * JPA repository
 */
public interface BankAccountJpaRepository extends JpaRepository<BankAccount, Long> {

	Optional<BankAccount> findByAccountNumber(AccountNumber accountNumber);

}
