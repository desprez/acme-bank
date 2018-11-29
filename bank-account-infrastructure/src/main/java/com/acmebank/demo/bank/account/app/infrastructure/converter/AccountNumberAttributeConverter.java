package com.acmebank.demo.bank.account.app.infrastructure.converter;

import javax.persistence.AttributeConverter;

import com.acmebank.demo.bank.account.app.domain.vo.AccountNumber;

/**
 * AccountNumber to String, String to AccountNumber JPA converter.
 */
public class AccountNumberAttributeConverter implements AttributeConverter<AccountNumber, String> {

	@Override
	public String convertToDatabaseColumn(final AccountNumber attribute) {
		if (attribute == null) {
			return null;
		}
		return attribute.getValue();
	}

	@Override
	public AccountNumber convertToEntityAttribute(final String dbData) {
		if (dbData == null) {
			return null;
		}
		return AccountNumber.of(dbData);
	}

}