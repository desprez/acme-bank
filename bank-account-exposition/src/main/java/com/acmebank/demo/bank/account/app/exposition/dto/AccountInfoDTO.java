package com.acmebank.demo.bank.account.app.exposition.dto;

import java.math.BigDecimal;
import java.util.Objects;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * AccountInfo DTO.
 */
@Validated
public class AccountInfoDTO {

	@JsonProperty("lowerLimit")
	private BigDecimal lowerLimit = null;

	@JsonProperty("currency")
	private CurrencyEnum currency = null;

	@JsonProperty("iban")
	private String iban = null;

	@JsonProperty("active")
	private Boolean active = null;

	public BigDecimal getLowerLimit() {
		return lowerLimit;
	}

	public void setLowerLimit(final BigDecimal lowerLimit) {
		this.lowerLimit = lowerLimit;
	}

	public CurrencyEnum getCurrency() {
		return currency;
	}

	public void setCurrency(final CurrencyEnum currency) {
		this.currency = currency;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(final String iban) {
		this.iban = iban;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(final Boolean active) {
		this.active = active;
	}

	@Override
	public boolean equals(final java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final AccountInfoDTO accountInfo = (AccountInfoDTO) o;
		return Objects.equals(iban, accountInfo.iban) && Objects.equals(lowerLimit, accountInfo.lowerLimit)
				&& Objects.equals(currency, accountInfo.currency)
				&& Objects.equals(active, accountInfo.active);
	}

	@Override
	public int hashCode() {
		return Objects.hash(iban, lowerLimit, currency, active);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("class AccountInfoDTO {\n");
		sb.append("    lowerLimit: ").append(toIndentedString(lowerLimit)).append("\n");
		sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
		sb.append("    active: ").append(toIndentedString(active)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 */
	private String toIndentedString(final java.lang.Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}
}
