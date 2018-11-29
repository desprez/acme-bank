package com.acmebank.demo.bank.account.app.exposition.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Test;

/**
 * Simple JUnit Test for Dtos.
 */
public class DTOTest {

	private static final CurrencyEnum EURO = CurrencyEnum.EUR;
	private static final BigDecimal AMOUNT = BigDecimal.valueOf(100);
	private static final String IBAN = "FR9571471028571296961475535";

	private AccountInfoDTO fixtureWithAccountInfoDTO() {
		final AccountInfoDTO dto = new AccountInfoDTO();
		dto.setIban(IBAN);
		dto.setLowerLimit(AMOUNT);
		dto.setCurrency(EURO);
		dto.setActive(Boolean.TRUE);
		return dto;
	}

	@Test
	public void testEqualsAccountInfoDTO() {
		final AccountInfoDTO dto1 = fixtureWithAccountInfoDTO();
		final AccountInfoDTO dto2 = fixtureWithAccountInfoDTO();
		assertTrue(dto1.equals(dto1));
		assertFalse(dto1.equals(null));
		assertFalse(dto1.equals(new AccountInfoDTO()));
		assertTrue(dto1.equals(dto2));
		assertEquals(dto1.hashCode(), dto2.hashCode());
	}

	@Test
	public void testAccountInfoDTOToString() {
		assertNotNull(new AccountInfoDTO().toString());
		assertNotNull(fixtureWithAccountInfoDTO().toString());
	}

	private AccountingRequestDTO fixtureWithAccountingRequestDTO() {
		return new AccountingRequestDTO().lowerLimit(AMOUNT).currency(EURO);
	}

	@Test
	public void testEqualsAccountingRequestDTO() {
		final AccountingRequestDTO dto1 = fixtureWithAccountingRequestDTO();
		final AccountingRequestDTO dto2 = fixtureWithAccountingRequestDTO();
		assertTrue(dto1.equals(dto1));
		assertFalse(dto1.equals(null));
		assertFalse(dto1.equals(new AccountingRequestDTO()));
		assertTrue(dto1.equals(dto2));
		assertEquals(dto1.hashCode(), dto2.hashCode());
	}

	@Test
	public void testAccountingRequestDTOToString() {
		assertNotNull(new AccountingRequestDTO().toString());
		assertNotNull(fixtureWithAccountingRequestDTO().toString());
	}

	private DepositDTO fixtureWithDepositDTO() {
		return new DepositDTO().amount(AMOUNT).currency(EURO);
	}

	@Test
	public void testEqualsDepositDTO() {
		final DepositDTO dto1 = fixtureWithDepositDTO();
		final DepositDTO dto2 = fixtureWithDepositDTO();
		assertTrue(dto1.equals(dto1));
		assertFalse(dto1.equals(null));
		assertFalse(dto1.equals(new DepositDTO()));
		assertTrue(dto1.equals(dto2));
		assertEquals(dto1.hashCode(), dto2.hashCode());
	}

	@Test
	public void testDepositDTOToString() {
		assertNotNull(new DepositDTO().toString());
		assertNotNull(fixtureWithDepositDTO().toString());
	}

	private WithdrawalDTO fixtureWithWithdrawalDTO() {
		return new WithdrawalDTO().amount(AMOUNT).currency(EURO);
	}

	@Test
	public void testEqualsWithdrawalDTO() {
		final WithdrawalDTO dto1 = fixtureWithWithdrawalDTO();
		final WithdrawalDTO dto2 = fixtureWithWithdrawalDTO();
		assertTrue(dto1.equals(dto1));
		assertFalse(dto1.equals(null));
		assertFalse(dto1.equals(new WithdrawalDTO()));
		assertTrue(dto1.equals(dto2));
		assertEquals(dto1.hashCode(), dto2.hashCode());
	}

	@Test
	public void testWithdrawalDTOToString() {
		assertNotNull(new WithdrawalDTO().toString());
		assertNotNull(fixtureWithWithdrawalDTO().toString());
	}

	private TransferDTO fixtureWithTransferDTO() {
		return new TransferDTO().ibanTo(IBAN).amount(AMOUNT).currency(EURO);
	}

	@Test
	public void testEqualsTransferDTO() {
		final TransferDTO dto1 = fixtureWithTransferDTO();
		final TransferDTO dto2 = fixtureWithTransferDTO();
		assertTrue(dto1.equals(dto1));
		assertFalse(dto1.equals(null));
		assertFalse(dto1.equals(new TransferDTO()));
		assertTrue(dto1.equals(dto2));
		assertEquals(dto1.hashCode(), dto2.hashCode());
	}

	@Test
	public void testTransferDTOToString() {
		assertNotNull(new TransferDTO().toString());
		assertNotNull(fixtureWithTransferDTO().toString());
	}

	private BalanceInfoDTO fixtureWithBalanceInfoDTO() {
		return new BalanceInfoDTO().amount(BigDecimal.valueOf(100)).currency(CurrencyEnum.EUR);
	}

	@Test
	public void testEqualsBalanceInfoDTO() {
		final BalanceInfoDTO dto1 = fixtureWithBalanceInfoDTO();
		final BalanceInfoDTO dto2 = fixtureWithBalanceInfoDTO();
		assertTrue(dto1.equals(dto1));
		assertFalse(dto1.equals(null));
		assertFalse(dto1.equals(new BalanceInfoDTO()));
		assertTrue(dto1.equals(dto2));
		assertEquals(dto1.hashCode(), dto2.hashCode());
	}

	@Test
	public void testBalanceInfoDTOToString() {
		assertNotNull(new BalanceInfoDTO().toString());
		assertNotNull(fixtureWithBalanceInfoDTO().toString());
	}
}
