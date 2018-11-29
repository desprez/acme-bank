package com.acmebank.demo.bank.account.app.exposition.dto;

import java.math.BigDecimal;
import java.util.Objects;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * AccountingRequest DTO.
 */
@Validated
public class AccountingRequestDTO {

	@JsonProperty("lowerLimit")
	private BigDecimal lowerLimit = null;

	@JsonProperty("currency")
	private CurrencyEnum currency = null;

	public AccountingRequestDTO lowerLimit(final BigDecimal lowerLimit) {
		this.lowerLimit = lowerLimit;
		return this;
	}

	/**
	 * Balance lower Limit minimum: -100 maximum: 10000
	 *
	 * @return lowerLimit
	 **/
	@ApiModelProperty(example = "0.0", required = true, value = "Balance lower Limit")
	@NotNull
	@DecimalMin("-100")
	@DecimalMax("10000")
	public BigDecimal getLowerLimit() {
		return lowerLimit;
	}

	public void setLowerLimit(final BigDecimal lowerLimit) {
		this.lowerLimit = lowerLimit;
	}

	public AccountingRequestDTO currency(final CurrencyEnum currency) {
		this.currency = currency;
		return this;
	}

	/**
	 * Account Currency
	 *
	 * @return currency
	 **/
	@ApiModelProperty(required = true, value = "Account Currency")
	@NotNull
	//@Pattern(regexp = "^[A-Z0-9]{3}$")
	public CurrencyEnum getCurrency() {
		return currency;
	}

	public void setCurrency(final CurrencyEnum currency) {
		this.currency = currency;
	}

	@Override
	public boolean equals(final java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final AccountingRequestDTO accountingRequest = (AccountingRequestDTO) o;
		return Objects.equals(lowerLimit, accountingRequest.lowerLimit)
				&& Objects.equals(currency, accountingRequest.currency);
	}

	@Override
	public int hashCode() {
		return Objects.hash(lowerLimit, currency);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("class AccountingRequest {\n");
		sb.append("    lowerLimit: ").append(toIndentedString(lowerLimit)).append("\n");
		sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
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
