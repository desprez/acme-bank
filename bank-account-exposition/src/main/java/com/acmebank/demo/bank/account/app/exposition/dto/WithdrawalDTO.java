package com.acmebank.demo.bank.account.app.exposition.dto;

import java.math.BigDecimal;
import java.util.Objects;

import org.springframework.validation.annotation.Validated;

/**
 * Withdrawal DTO
 */
@Validated
public class WithdrawalDTO extends AbstractOperation {

	public WithdrawalDTO amount(final BigDecimal amount) {
		this.amount = amount;
		return this;
	}

	public WithdrawalDTO currency(final CurrencyEnum currency) {
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
		final WithdrawalDTO withdrawal = (WithdrawalDTO) o;
		return Objects.equals(amount, withdrawal.amount) && Objects.equals(currency, withdrawal.currency);
	}

}
