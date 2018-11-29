package com.acmebank.demo.bank.account.app.exposition.adapter;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;

import org.javamoney.moneta.Money;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.acmebank.demo.bank.account.app.exposition.dto.AccountingRequestDTO;
import com.acmebank.demo.bank.account.app.exposition.dto.CurrencyEnum;
import com.acmebank.demo.bank.account.app.exposition.dto.DepositDTO;
import com.acmebank.demo.bank.account.app.exposition.dto.TransferDTO;
import com.acmebank.demo.bank.account.app.exposition.dto.WithdrawalDTO;
import com.acmebank.demo.bank.account.app.exposition.mapper.MoneyMapper;

/**
 * Simple JUnit Test for MoneyMapper
 */
public class MoneyMapperTest {

	@Autowired
	private final MoneyMapper moneyMapper = new MoneyMapper();

	@Test
	public void testMapDepositDTOShouldSuccess() {
		// Given
		final DepositDTO deposit = new DepositDTO();
		deposit.setAmount(BigDecimal.valueOf(123.33));
		deposit.setCurrency(CurrencyEnum.EUR);

		// When
		final Money result = moneyMapper.map(deposit);
		// Then

		assertThat(result.toString(), equalTo("EUR 123.33"));
	}

	@Test
	public void testMapWithdrawalDTOShouldSuccess() {
		// Given
		final WithdrawalDTO withdrawal = new WithdrawalDTO();
		withdrawal.setAmount(BigDecimal.valueOf(123.33));
		withdrawal.setCurrency(CurrencyEnum.EUR);

		// When
		final Money result = moneyMapper.map(withdrawal);

		// Then
		assertThat(result.toString(), equalTo("EUR 123.33"));
	}

	@Test
	public void testMapTransferDTOShouldSuccess() {
		// Given
		final TransferDTO transfer = new TransferDTO();
		transfer.setAmount(BigDecimal.valueOf(123.33));
		transfer.setCurrency(CurrencyEnum.EUR);

		// When
		final Money result = moneyMapper.map(transfer);

		// Then
		assertThat(result.toString(), equalTo("EUR 123.33"));
	}

	@Test
	public void testMapAccountingRequestDTOShouldSuccess() {
		// Given
		final AccountingRequestDTO accountingRequest = new AccountingRequestDTO();
		accountingRequest.setLowerLimit(BigDecimal.valueOf(123.33));
		accountingRequest.setCurrency(CurrencyEnum.EUR);

		// When
		final Money result = moneyMapper.map(accountingRequest);

		// Then
		assertThat(result.toString(), equalTo("EUR 123.33"));
	}

}
