package com.acmebank.demo.bank.account.app.exposition.dto;

import java.math.BigDecimal;
import java.util.Objects;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * Super class of operations Dto.
 */
public abstract class AbstractOperation {

	@JsonProperty("amount")
	protected BigDecimal amount = null;

	@JsonProperty("currency")
	protected CurrencyEnum currency = null;

	/**
	 * Amount minimum: 0 maximum: 100000
	 *
	 * @return amount
	 **/
	@ApiModelProperty(example = "200.0", required = true, value = "Amount")
	@NotNull
	@DecimalMin("0")
	@DecimalMax("100000")
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(final BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * Get currency
	 *
	 * @return currency
	 **/
	@ApiModelProperty(required = true, value = "")
	@NotNull
	// @Pattern(regexp = "^[A-Z0-9]{3}$")
	public CurrencyEnum getCurrency() {
		return currency;
	}

	public void setCurrency(final CurrencyEnum currency) {
		this.currency = currency;
	}

	@Override
	public int hashCode() {
		return Objects.hash(amount, currency);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("class Operation {\n");
		sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
		sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 */
	protected String toIndentedString(final java.lang.Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}

}
