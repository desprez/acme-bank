package com.acmebank.demo.bank.account.app.domain.vo;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * Simple "fake" random account Number generator
 */
public final class RandomIBANGenerator {

	private static final int MOD = 97;
	private static final long MAX = 999999999;
	private static final String BANK_CODE = "30004";
	private static final String FRANCE_CODE = "FR";
	private static final String DEFAULT_CHECK_DIGIT = "00";

	public static String generate() {
		final StringBuilder sb = new StringBuilder();
		sb.append(FRANCE_CODE);
		sb.append(DEFAULT_CHECK_DIGIT);
		sb.append(BANK_CODE);
		sb.append(RandomStringUtils.randomNumeric(18));

		final String formattedIban = sb.toString();
		final String checkDigit = calculateCheckDigit(formattedIban);

		// replace default check digit with calculated check digit
		final String ibanValue = replaceCheckDigit(formattedIban, checkDigit);
		return ibanValue;
	}

	private static String calculateCheckDigit(final String iban) {
		final String reformattedIban = replaceCheckDigit(iban, DEFAULT_CHECK_DIGIT);
		final int modResult = calculateMod(reformattedIban);
		final int checkDigitIntValue = 98 - modResult;
		final String checkDigit = Integer.toString(checkDigitIntValue);
		return checkDigitIntValue > 9 ? checkDigit : "0" + checkDigit;
	}

	static String replaceCheckDigit(final String iban, final String checkDigit) {
		return iban.substring(0, 0 + 2) + checkDigit + iban.substring(2 + 2);
	}

	private static int calculateMod(final String iban) {
		final String reformattedIban = iban.substring(2 + 2) + iban.substring(0, 0 + 2 + 2);
		long total = 0;
		for (int i = 0; i < reformattedIban.length(); i++) {
			final int numericValue = Character.getNumericValue(reformattedIban.charAt(i));
			if (numericValue < 0 || numericValue > 35) {
				throw new IllegalArgumentException(String.format("Invalid Character[%d] = '%d'", i, numericValue));
			}
			total = (numericValue > 9 ? total * 100 : total * 10) + numericValue;

			if (total > MAX) {
				total = total % MOD;
			}

		}
		return (int) (total % MOD);
	}

}
