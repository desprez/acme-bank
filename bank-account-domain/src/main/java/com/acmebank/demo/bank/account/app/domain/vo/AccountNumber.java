package com.acmebank.demo.bank.account.app.domain.vo;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.validator.routines.IBANValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acmebank.demo.bank.account.app.domain.exception.InvalidAccountNumberException;

/**
 * AccountNumber value objet
 */
public class AccountNumber implements Serializable {
	private static final long serialVersionUID = 6888997624020049876L;

	private static final Logger log = LoggerFactory.getLogger(AccountNumber.class);

	private final String value;

	private AccountNumber(final String value) {
		this.value = value;
	}

	/**
	 * Factory method
	 *
	 * @param value
	 *            String representation of the iban
	 * @return an AccountNumber instance
	 */
	public static AccountNumber of(final String value) {
		if (IBANValidator.getInstance().isValid(value)) {
			return new AccountNumber(value);
		} else {
			throw new InvalidAccountNumberException();
		}
	}

	public String getValue() {
		return value;
	}

	/**
	 * Simple "fake" random account Number generator
	 *
	 * @return a new account Number
	 */
	public static AccountNumber generate() {

		final String ibanValue = RandomIBANGenerator.generate();

		log.debug("Account number {} generated", ibanValue);

		return of(ibanValue);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (value == null ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final AccountNumber other = (AccountNumber) obj;
		return new EqualsBuilder().append(value, other.value).isEquals();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append(value).toString();
	}
}
