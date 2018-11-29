package com.acmebank.demo.bank.account.app.infrastructure.converter;

import javax.persistence.AttributeConverter;

import org.javamoney.moneta.Money;

/**
 * Money to String, String to Money JPA converter.
 */
public class MoneyAttributeConverter implements AttributeConverter<Money, String> {

	@Override
	public String convertToDatabaseColumn(final Money attribute) {
		if (attribute == null) {
			return null;
		}
		return Money.from(attribute).toString();
	}

	@Override
	public Money convertToEntityAttribute(final String dbData) {
		if (dbData == null) {
			return null;
		}
		return Money.parse(dbData);
	}

}