package com.acmebank.demo.bank.account.app.exposition.adapter;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

import org.javamoney.moneta.Money;
import org.junit.Test;

import com.acmebank.demo.bank.account.app.exposition.dto.BalanceInfoDTO;
import com.acmebank.demo.bank.account.app.exposition.dto.CurrencyEnum;
import com.acmebank.demo.bank.account.app.exposition.mapper.BalanceMapper;

/**
 * Simple JUnit Test for BalanceMapper
 */
public class BalanceMapperTest {

	private final BalanceMapper balanceMapper = new BalanceMapper();

	@Test
	public void testMapMoneyToBalanceInfoDTOShouldSuccess() {
		// Given
		final CurrencyUnit currencyUnit = Monetary.getCurrency("EUR");
		final Money money = Money.of(123.33, currencyUnit);

		// When
		final BalanceInfoDTO result = balanceMapper.map(money);

		// Then
		assertThat(result.getCurrency(), equalTo(CurrencyEnum.EUR));
		assertThat(result.getAmount(), closeTo(BigDecimal.valueOf(123.33), BigDecimal.ZERO));
	}

}
