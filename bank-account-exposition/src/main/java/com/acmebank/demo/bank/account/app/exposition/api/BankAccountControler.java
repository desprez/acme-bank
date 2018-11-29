package com.acmebank.demo.bank.account.app.exposition.api;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import org.javamoney.moneta.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.acmebank.demo.bank.account.app.domain.entity.BankAccount;
import com.acmebank.demo.bank.account.app.domain.vo.AccountNumber;
import com.acmebank.demo.bank.account.app.exposition.dto.AccountInfoDTO;
import com.acmebank.demo.bank.account.app.exposition.dto.AccountingRequestDTO;
import com.acmebank.demo.bank.account.app.exposition.dto.BalanceInfoDTO;
import com.acmebank.demo.bank.account.app.exposition.dto.DepositDTO;
import com.acmebank.demo.bank.account.app.exposition.dto.TransferDTO;
import com.acmebank.demo.bank.account.app.exposition.dto.WithdrawalDTO;
import com.acmebank.demo.bank.account.app.exposition.mapper.AccountMapper;
import com.acmebank.demo.bank.account.app.exposition.mapper.BalanceMapper;
import com.acmebank.demo.bank.account.app.exposition.mapper.MoneyMapper;
import com.acmebank.demo.bank.account.app.service.BankAccountServicePort;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Controller example
 */
@RestController
@Validated
public class BankAccountControler {

	private static final Logger log = LoggerFactory.getLogger(BankAccountControler.class);

	private final BankAccountServicePort bankAccountService;

	private final BalanceMapper balanceMapper;

	private final MoneyMapper moneyMapper;

	private final AccountMapper accountMappper;

	public BankAccountControler(final BankAccountServicePort bankAccountService, final BalanceMapper balanceMapper,
			final MoneyMapper moneyMapper, final AccountMapper accountMappper) {
		this.bankAccountService = bankAccountService;
		this.balanceMapper = balanceMapper;
		this.moneyMapper = moneyMapper;
		this.accountMappper = accountMappper;
	}

	@ApiOperation(value = "createAccounting", nickname = "createAccountUsingPOST", notes = "", response = AccountInfoDTO.class, tags = {
			"bank-accounting-resource", })
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Created"),
			@ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/api/accountings", produces = { "application/json" }, consumes = {
	"application/json" }, method = RequestMethod.POST)
	public ResponseEntity<AccountInfoDTO> createAccountUsingPOST(
			@ApiParam(value = "accounting request", required = true) @Valid @RequestBody final AccountingRequestDTO accountingRequest) {

		log.info("createAccountUsingPOST:{}", accountingRequest.toString());

		final Money accountInfo = moneyMapper.map(accountingRequest);

		final BankAccount account = bankAccountService.createAccount(accountInfo);

		final AccountInfoDTO accountDto = accountMappper.map(account);

		return new ResponseEntity<AccountInfoDTO>(accountDto, HttpStatus.CREATED);
	}

	@ApiOperation(value = "getBalance", nickname = "getBalanceUsingGET", notes = "", response = BalanceInfoDTO.class, tags = {
			"bank-accounting-resource", })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = BalanceInfoDTO.class),
			@ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/api/accountings/{iban}/balance", produces = {
	"application/json" }, method = RequestMethod.GET)
	public ResponseEntity<BalanceInfoDTO> getBalanceUsingGET(
			@Pattern(regexp = "^[A-Z0-9]{27}$") @ApiParam(value = "iban", required = true) @PathVariable("iban") final String iban) {

		log.info("getBalanceUsingGET: {}", iban);

		final BalanceInfoDTO balanceInfoDTO = balanceMapper.map(bankAccountService.getBalance(AccountNumber.of(iban)));

		return new ResponseEntity<BalanceInfoDTO>(balanceInfoDTO, HttpStatus.OK);
	}

	@ApiOperation(value = "deposit operation", nickname = "depositUsingPUT", notes = "", tags = {
			"bank-accounting-resource", })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/api/accountings/{iban}/deposit", produces = { "application/json" }, consumes = {
	"application/json" }, method = RequestMethod.PUT)
	ResponseEntity<Void> depositUsingPUT(
			@Pattern(regexp = "^[A-Z0-9]{27}$") @ApiParam(value = "iban", required = true) @PathVariable("iban") final String iban,
			@ApiParam(value = "deposit info", required = true) @Valid @RequestBody final DepositDTO deposit) {

		log.info("depositUsingPUT: {}, {}", iban, deposit);

		bankAccountService.deposit(AccountNumber.of(iban), moneyMapper.map(deposit));
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@ApiOperation(value = "transfer fund operation", nickname = "transfertUsingPUT", notes = "", tags = {
			"bank-accounting-resource", })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/api/accountings/{ibanfrom}/transfer", produces = { "application/json" }, consumes = {
	"application/json" }, method = RequestMethod.PUT)
	public ResponseEntity<Void> transferUsingPUT(
			@Pattern(regexp = "^[A-Z0-9]{27}$") @ApiParam(value = "Account 'To' IBAN", required = true) @PathVariable("ibanfrom") final String ibanfrom,
			@ApiParam(value = "transfer info", required = true) @Valid @RequestBody final TransferDTO transfer) {

		log.info("transferUsingPUT: {}, {}", ibanfrom, transfer);

		bankAccountService.transfer(AccountNumber.of(ibanfrom), AccountNumber.of(transfer.getIbanTo()),
				moneyMapper.map(transfer));

		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@ApiOperation(value = "withdrawal operation", nickname = "withdrawalUsingPUT", notes = "", tags = {
			"bank-accounting-resource", })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/api/accountings/{iban}/withdrawal", produces = { "application/json" }, consumes = {
	"application/json" }, method = RequestMethod.PUT)
	public ResponseEntity<Void> withdrawalUsingPUT(
			@Pattern(regexp = "^[A-Z0-9]{27}$") @ApiParam(value = "iban", required = true) @PathVariable("iban") final String iban,
			@ApiParam(value = "withdrawal info", required = true) @Valid @RequestBody final WithdrawalDTO withdrawal) {

		log.info("withdrawalUsingPUT: {}, {}", iban, withdrawal);

		bankAccountService.withdraw(AccountNumber.of(iban), moneyMapper.map(withdrawal));

		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@ApiOperation(value = "close operation", nickname = "closeUsingPUT", notes = "", tags = {
			"bank-accounting-resource", })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 409, message = "Conflict"),
			@ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/api/accountings/{iban}/close", produces = { "application/json" }, consumes = {
	"application/json" }, method = RequestMethod.PUT)
	public ResponseEntity<Void> closeUsingPUT(
			@Pattern(regexp = "^[A-Z0-9]{27}$") @ApiParam(value = "iban", required = true) @PathVariable("iban") final String iban) {

		log.info("closeUsingPUT: {}", iban);

		bankAccountService.close(AccountNumber.of(iban));

		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@ApiOperation(value = "suspend operation", nickname = "suspendUsingPUT", notes = "", tags = {
			"bank-accounting-resource", })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 409, message = "Conflict"),
			@ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/api/accountings/{iban}/suspend", produces = { "application/json" }, consumes = {
	"application/json" }, method = RequestMethod.PUT)
	public ResponseEntity<Void> suspendUsingPUT(
			@Pattern(regexp = "^[A-Z0-9]{27}$") @ApiParam(value = "iban", required = true) @PathVariable("iban") final String iban) {

		log.info("suspendUsingPUT: {}", iban);

		bankAccountService.suspend(AccountNumber.of(iban));

		return new ResponseEntity<Void>(HttpStatus.OK);
	}
}
