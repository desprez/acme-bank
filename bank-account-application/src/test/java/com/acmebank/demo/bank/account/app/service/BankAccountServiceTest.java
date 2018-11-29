package com.acmebank.demo.bank.account.app.service;

import static javax.money.Monetary.getCurrency;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.acmebank.demo.bank.account.app.domain.entity.BankAccount;
import com.acmebank.demo.bank.account.app.domain.exception.InsufficientBalanceException;
import com.acmebank.demo.bank.account.app.domain.exception.NotZeroBalanceException;
import com.acmebank.demo.bank.account.app.domain.repository.BankAccountRepositoryPort;
import com.acmebank.demo.bank.account.app.domain.vo.AccountNumber;
import com.acmebank.demo.bank.account.app.domain.vo.AccountStatus;
import com.acmebank.demo.bank.account.app.service.config.TestConfiguration;

/**
 * Simple JUnit Test for BankAccountService.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { TestConfiguration.class })
public class BankAccountServiceTest {

	private static final Money ZERO_BALANCE = Money.of(0, getCurrency("EUR"));
	private static final AccountNumber IBAN_INSUFICIENT = AccountNumber.of("FR1194431796668004498223348");
	private static final AccountNumber IBAN_DEBIT = AccountNumber.of("FR9396259501233517720957155");
	private static final AccountNumber IBAN_CREDIT = AccountNumber.of("FR1917580123775267305836959");

	@Autowired
	private BankAccountServicePort accountService;

	@MockBean
	private BankAccountRepositoryPort accountRepositoryMock;

	private BankAccount insufficientAccount;
	private Optional<BankAccount> optInsufficientAccount;
	private BankAccount debitAccount;
	private Optional<BankAccount> optDebitAccount;
	private BankAccount creditAccount;
	private Optional<BankAccount> optCreditAccount;

	@Test
	public void contextLoads() {
		assertNotNull(accountService);
	}

	@Before
	public void initAccounts() {
		insufficientAccount = new BankAccount.Builder().accountNumber(IBAN_INSUFICIENT).lowerLimit(ZERO_BALANCE)
				.build();
		optInsufficientAccount = Optional.ofNullable(insufficientAccount);
		debitAccount = new BankAccount.Builder().accountNumber(IBAN_DEBIT).lowerLimit(ZERO_BALANCE).build();
		debitAccount.credit(Money.of(1000, getCurrency("EUR")));
		optDebitAccount = Optional.ofNullable(debitAccount);
		creditAccount = new BankAccount.Builder().accountNumber(IBAN_CREDIT).lowerLimit(ZERO_BALANCE).build();
		creditAccount.credit(Money.of(500, getCurrency("EUR")));
		optCreditAccount = Optional.ofNullable(creditAccount);
	}

	@Test
	public void testWithdrawShouldSuccess() {
		when(accountRepositoryMock.findByAccountNumber(IBAN_DEBIT)).thenReturn(optDebitAccount);
		accountService.withdraw(IBAN_DEBIT, Money.of(500, getCurrency("EUR")));
		assertThat(debitAccount.getBalance(), equalTo(Money.of(500, getCurrency("EUR"))));
	}

	@Test
	public void testTransferShouldSuccess() {
		when(accountRepositoryMock.findByAccountNumber(IBAN_DEBIT)).thenReturn(optDebitAccount);
		when(accountRepositoryMock.findByAccountNumber(IBAN_CREDIT)).thenReturn(optCreditAccount);
		accountService.transfer(IBAN_DEBIT, IBAN_CREDIT, Money.of(100, getCurrency("EUR")));
		assertThat(debitAccount.getBalance(), equalTo(Money.of(900, getCurrency("EUR"))));
		assertThat(creditAccount.getBalance(), equalTo(Money.of(600, getCurrency("EUR"))));
	}

	@Test(expected = InsufficientBalanceException.class)
	public void testWithdrawInsufficientBalanceShouldFails() throws Exception {
		when(accountRepositoryMock.findByAccountNumber(IBAN_INSUFICIENT)).thenReturn(optInsufficientAccount);
		accountService.withdraw(IBAN_INSUFICIENT, Money.of(500, getCurrency("EUR")));
		fail("InsufficientBalanceException expected here");
	}

	@Test(expected = NotZeroBalanceException.class)
	public void testCloseNotZeroBalanceShouldFails() {
		when(accountRepositoryMock.findByAccountNumber(IBAN_DEBIT)).thenReturn(optDebitAccount);
		accountService.close(IBAN_DEBIT);
		fail("InsufficientBalanceException expected here");
	}

	@Test
	public void testCloseShouldSuccess() {
		when(accountRepositoryMock.findByAccountNumber(IBAN_INSUFICIENT)).thenReturn(optInsufficientAccount);
		accountService.close(IBAN_INSUFICIENT);
		assertThat(insufficientAccount.getStatus(), equalTo(AccountStatus.CLOSED));
	}

	@Test
	public void testSuspendShouldSuccess() {
		when(accountRepositoryMock.findByAccountNumber(IBAN_DEBIT)).thenReturn(optDebitAccount);
		accountService.suspend(IBAN_DEBIT);
		assertThat(debitAccount.getStatus(), equalTo(AccountStatus.SUSPENDED));
	}

}