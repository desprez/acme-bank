package com.acmebank.demo.bank.account.app.infrastructure.converter;

import javax.persistence.AttributeConverter;

import com.acmebank.demo.bank.account.app.domain.vo.AccountStatus;

/**
 * AccountStatus to String, String to AccountStatus JPA converter.
 */
public class AccountStatusAttributeConverter implements AttributeConverter<AccountStatus, String> {

	@Override
	public String convertToDatabaseColumn(final AccountStatus accountStatus) {
		switch (accountStatus) {
		case ACTIVE:
			return "A";

		case CLOSED:
			return "C";

		case SUSPENDED:
			return "S";

		default:
			throw new IllegalArgumentException("Invalid AccountStatus: " + accountStatus);
		}
	}

	@Override
	public AccountStatus convertToEntityAttribute(final String status) {
		switch (status) {
		case "A":
			return AccountStatus.ACTIVE;

		case "C":
			return AccountStatus.CLOSED;

		case "S":
			return AccountStatus.SUSPENDED;

		default:
			throw new IllegalArgumentException("Invalid AccountStatus code: " + status);
		}
	}
}