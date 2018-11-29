package com.acmebank.demo.bank.account.app.exposition.mapper;

import java.math.BigDecimal;

import org.javamoney.moneta.Money;
import org.springframework.stereotype.Component;

import com.acmebank.demo.bank.account.app.exposition.dto.BalanceInfoDTO;
import com.acmebank.demo.bank.account.app.exposition.dto.CurrencyEnum;

/**
 * Bean Mapper to convert Money VO to AccountInfo DTO.
 */
@Component
public class BalanceMapper {

	public BalanceInfoDTO map(final Money money) {
		final BalanceInfoDTO balanceInfo = new BalanceInfoDTO();
		balanceInfo.amount(money.getNumber().numberValueExact(BigDecimal.class));
		balanceInfo.currency(CurrencyEnum.fromValue(money.getCurrency().getCurrencyCode()));
		return balanceInfo;
	}

}
