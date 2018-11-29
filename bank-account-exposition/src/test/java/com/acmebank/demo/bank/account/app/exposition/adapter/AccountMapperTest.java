package com.acmebank.demo.bank.account.app.exposition.adapter;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;

import javax.money.Monetary;

import org.javamoney.moneta.Money;
import org.junit.Test;

import com.acmebank.demo.bank.account.app.domain.entity.BankAccount;
import com.acmebank.demo.bank.account.app.domain.vo.AccountNumber;
import com.acmebank.demo.bank.account.app.exposition.dto.AccountInfoDTO;
import com.acmebank.demo.bank.account.app.exposition.dto.CurrencyEnum;
import com.acmebank.demo.bank.account.app.exposition.mapper.AccountMapper;

/**
 * Simple JUnit Test for AccountMapper
 */
public class AccountMapperTest {

	private static final AccountNumber IBAN = AccountNumber.of("FR9202417332686507202418730");

	private final AccountMapper accountMapper = new AccountMapper();

	@Test
	public void testMapBankAccount2AccountInfoDTOShouldSuccess() {
		// Given
		final BankAccount account = new BankAccount.Builder().accountNumber(IBAN)
				.lowerLimit(Money.of(200.00, Monetary.getCurrency("EUR"))).build();

		// When
		final AccountInfoDTO result = accountMapper.map(account);

		// Then
		assertThat(result.getCurrency(), equalTo(CurrencyEnum.EUR));
		assertThat(result.getLowerLimit(), closeTo(BigDecimal.valueOf(200), BigDecimal.ZERO));
		assertThat(result.getActive(), equalTo(Boolean.TRUE));
	}

}
