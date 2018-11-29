package com.acmebank.demo.bank.account.app.exposition.mapper;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

import org.javamoney.moneta.Money;
import org.springframework.stereotype.Component;

import com.acmebank.demo.bank.account.app.exposition.dto.AccountingRequestDTO;
import com.acmebank.demo.bank.account.app.exposition.dto.DepositDTO;
import com.acmebank.demo.bank.account.app.exposition.dto.TransferDTO;
import com.acmebank.demo.bank.account.app.exposition.dto.WithdrawalDTO;

/**
 * Bean Mapper to convert DTO Amounts to Money Value Object.
 */
@Component
public class MoneyMapper {

	public Money map(final DepositDTO deposit) {

		final CurrencyUnit currencyUnit = Monetary.getCurrency(deposit.getCurrency().name());

		final Money moneyof = Money.of(deposit.getAmount(), currencyUnit);

		return moneyof;
	}

	public Money map(final WithdrawalDTO withdraw) {

		final CurrencyUnit currencyUnit = Monetary.getCurrency(withdraw.getCurrency().name());

		final Money moneyof = Money.of(withdraw.getAmount(), currencyUnit);

		return moneyof;
	}

	public Money map(final TransferDTO transfer) {

		final CurrencyUnit currencyUnit = Monetary.getCurrency(transfer.getCurrency().name());

		final Money moneyof = Money.of(transfer.getAmount(), currencyUnit);

		return moneyof;
	}

	public Money map(final AccountingRequestDTO accountingRequest) {

		final CurrencyUnit currencyUnit = Monetary.getCurrency(accountingRequest.getCurrency().name());

		final Money moneyof = Money.of(accountingRequest.getLowerLimit(), currencyUnit);

		return moneyof;
	}

}
