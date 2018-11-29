package com.acmebank.demo.bank.account.app.exposition.dto;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * BalanceInfo DTO
 */
// @Validated
public class BalanceInfoDTO extends AbstractOperation {

	public BalanceInfoDTO amount(final BigDecimal amount) {
		this.amount = amount;
		return this;
	}

	public BalanceInfoDTO currency(final CurrencyEnum currency) {
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
		final BalanceInfoDTO balanceInfo = (BalanceInfoDTO) o;
		return Objects.equals(amount, balanceInfo.amount) && Objects.equals(currency, balanceInfo.currency);
	}

}
