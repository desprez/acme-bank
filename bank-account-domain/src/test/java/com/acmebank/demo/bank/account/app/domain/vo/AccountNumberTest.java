package com.acmebank.demo.bank.account.app.domain.vo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.acmebank.demo.bank.account.app.domain.exception.InvalidAccountNumberException;

public class AccountNumberTest {

	// ISO Code for France
	private static final String COUNTRY_CODE = "FR";

	// IBAN check digits
	private static final String CHECK_DIGITS = "14";

	// Bank Code
	private static final String BANK_CODE = "20041";

	// Branch Code (Code Guichet)
	private static final String BRANCH_CODE = "01005";

	// BBAN
	private static final String BBAN = "0500013M026";

	// National check digits (clé RIB)
	private static final String NATIONAL_CHECK_DIGITS = "06";

	private static final String VALID_IBAN0 = COUNTRY_CODE + CHECK_DIGITS + BANK_CODE + BRANCH_CODE + BBAN
			+ NATIONAL_CHECK_DIGITS;
	private static final String INVALID_IBAN1 = "FR7630004440547963719290653";
	private static final String VALID_IBAN2 = "AL36442788709271283994894168";

	@Rule
	public ExpectedException expectedEx = ExpectedException.none();

	@Test
	public void testOfWithValidIBANShouldSuccess() {
		assertEquals(VALID_IBAN2, AccountNumber.of(VALID_IBAN2).getValue());
	}

	@Test(expected = InvalidAccountNumberException.class)
	public void testOfInvalidIBANShouldFail() {
		assertNull(AccountNumber.of(INVALID_IBAN1));
	}

	@Test
	public void testGetValueWithValidIBANShouldSuccess() {
		assertEquals(VALID_IBAN0, AccountNumber.of(VALID_IBAN0).getValue());
	}

	@Test
	public void testGenerate() {
		assertNotNull(AccountNumber.generate());
	}

	@Test
	public void testEqualsObject() {
		assertTrue(AccountNumber.of(VALID_IBAN2).equals(AccountNumber.of(VALID_IBAN2)));
		assertFalse(AccountNumber.of(VALID_IBAN0).equals(AccountNumber.of(VALID_IBAN2)));
	}

	@Test
	public void testToString() {
		assertNotNull(AccountNumber.of(VALID_IBAN2).toString());
	}

}
