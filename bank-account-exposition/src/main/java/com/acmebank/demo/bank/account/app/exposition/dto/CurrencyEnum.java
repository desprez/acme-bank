package com.acmebank.demo.bank.account.app.exposition.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets currency
 */
public enum CurrencyEnum {

	EUR("EUR"),

	USD("USD"),

	GBP("GBP");

	private final String value;

	CurrencyEnum(final String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static CurrencyEnum fromValue(final String text) {
		for (final CurrencyEnum b : CurrencyEnum.values()) {
			if (String.valueOf(b.value).equals(text)) {
				return b;
			}
		}
		return null;
	}
}