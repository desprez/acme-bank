package com.acmebank.demo.bank.account.app.infrastructure;

import static javax.money.Monetary.getCurrency;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Optional;

import org.javamoney.moneta.Money;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.acmebank.demo.bank.account.app.config.TestConfiguration;
import com.acmebank.demo.bank.account.app.domain.entity.BankAccount;
import com.acmebank.demo.bank.account.app.domain.vo.AccountNumber;
import com.acmebank.demo.bank.account.app.domain.vo.AccountStatus;

/**
 * Simple JUnit Test for BankAccountService.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { TestConfiguration.class })
@DataJpaTest
public class BankAccountJpaRepositoryTest {

	private static final String EURO = "EUR";

	private static final AccountNumber IBAN_1 = AccountNumber.of("FR0818143231657010702566501");
	private static final AccountNumber IBAN_2 = AccountNumber.of("FR9202417332686507202418730");
	private static final AccountNumber IBAN_3 = AccountNumber.of("FR6748216867083088909670307");

	@Autowired
	private BankAccountRepositoryAdapter bankAccountRepository;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testFindByIbanExistingShouldSuccess() {

		final BankAccount account = new BankAccount.Builder().accountNumber(IBAN_1)
				.lowerLimit(Money.of(0, getCurrency(EURO))).build();
		account.credit(Money.of(500, getCurrency(EURO)));
		entityManager.persistAndFlush(account);

		final Optional<BankAccount> found = bankAccountRepository.findByAccountNumber(IBAN_1);
		assertThat(found.isPresent(), is(true));
	}

	@Test
	public void testFindByIbanUnknownShouldFail() {

		final Optional<BankAccount> found = bankAccountRepository.findByAccountNumber(IBAN_3);
		assertThat(found.isPresent(), is(false));
	}

	@Test
	public void testCreateAndFindShouldSuccess() {
		final BankAccount account = new BankAccount.Builder().accountNumber(IBAN_2)
				.lowerLimit(Money.of(0, getCurrency(EURO))).build();
		account.credit(Money.of(600, getCurrency(EURO)));
		final BankAccount created = bankAccountRepository.save(account);

		final BankAccount found = bankAccountRepository.findOne(created.getId());

		assertThat(found, is(notNullValue()));
		assertThat(found.getLowerLimit(), equalTo(Money.of(0, getCurrency(EURO))));
		assertThat(found.getBalance(), equalTo(Money.of(600, getCurrency(EURO))));
		assertThat(found.getAccountNumber(), equalTo(IBAN_2));
		assertThat(found.getStatus(), equalTo(AccountStatus.ACTIVE));
		assertThat(found.getHistory(), is(notNullValue()));
		assertThat(found.getHistory().getBalance(), equalTo(Money.of(0, getCurrency(EURO))));
	}

}
