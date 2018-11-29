package com.acmebank.demo.bank.account.app.exposition.dto;

import java.math.BigDecimal;
import java.util.Objects;

import javax.validation.constraints.Pattern;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * Transfer DTO
 */
@Validated
public class TransferDTO extends AbstractOperation {

	@JsonProperty("ibanTo")
	private String ibanTo = null;

	public TransferDTO ibanTo(final String ibanTo) {
		this.ibanTo = ibanTo;
		return this;
	}

	/**
	 * Account 'To' IBAN
	 *
	 * @return ibanTo
	 **/
	@ApiModelProperty(example = "FR6748216867083088909670307", value = "Account 'To' IBAN")
	@Pattern(regexp = "^[A-Z0-9]{27}$")
	public String getIbanTo() {
		return ibanTo;
	}

	public void setIbanTo(final String ibanTo) {
		this.ibanTo = ibanTo;
	}

	public TransferDTO amount(final BigDecimal amount) {
		this.amount = amount;
		return this;
	}

	public TransferDTO currency(final CurrencyEnum currency) {
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
		final TransferDTO transfert = (TransferDTO) o;
		return Objects.equals(ibanTo, transfert.ibanTo) && Objects.equals(amount, transfert.amount)
				&& Objects.equals(currency, transfert.currency);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("class Transfert {\n");
		sb.append("    ibanTo: ").append(toIndentedString(ibanTo)).append("\n");
		sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
		sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
		sb.append("}");
		return sb.toString();
	}

}
