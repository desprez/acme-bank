package com.acmebank.demo.bank.account.app.exposition.dto;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Deposit DTO
 */
// @Validated
public class DepositDTO extends AbstractOperation {

	public DepositDTO amount(final BigDecimal amount) {
		this.amount = amount;
		return this;
	}

	public DepositDTO currency(final CurrencyEnum currency) {
		this.currency = currency;
		return this;
	}

	@Override
	public boolean equals(final java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final DepositDTO deposit = (DepositDTO) o;
		return Objects.equals(amount, deposit.amount) && Objects.equals(currency, deposit.currency);
	}
}
