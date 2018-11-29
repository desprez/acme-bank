package com.acmebank.demo.bank.account.app.exposition.mapper;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.acmebank.demo.bank.account.app.domain.entity.BankAccount;
import com.acmebank.demo.bank.account.app.domain.vo.AccountStatus;
import com.acmebank.demo.bank.account.app.exposition.dto.AccountInfoDTO;
import com.acmebank.demo.bank.account.app.exposition.dto.CurrencyEnum;

/**
 * Bean Mapper to convert BankAccount Entity to AccountInfo DTO.
 */
@Component
public class AccountMapper {

	public AccountInfoDTO map(final BankAccount account) {
		final AccountInfoDTO accountInfo = new AccountInfoDTO();
		accountInfo.setIban(account.getAccountNumber().getValue());
		accountInfo.setLowerLimit(account.getLowerLimit().getNumber().numberValueExact(BigDecimal.class));
		accountInfo.setCurrency(CurrencyEnum.fromValue(account.getLowerLimit().getCurrency().getCurrencyCode()));
		accountInfo.setActive(AccountStatus.ACTIVE.equals(account.getStatus()));
		return accountInfo;
	}

}
