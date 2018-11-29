package com.acmebank.demo.bank.account.app.exposition.api.step;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.acmebank.demo.bank.account.app.exposition.api.BankAccountControler;
import com.acmebank.demo.bank.account.app.exposition.api.RestExceptionHandler;
import com.acmebank.demo.bank.account.app.exposition.dto.AccountInfoDTO;
import com.acmebank.demo.bank.account.app.exposition.dto.AccountingRequestDTO;
import com.acmebank.demo.bank.account.app.exposition.dto.BalanceInfoDTO;
import com.acmebank.demo.bank.account.app.exposition.dto.CurrencyEnum;
import com.acmebank.demo.bank.account.app.exposition.dto.DepositDTO;
import com.acmebank.demo.bank.account.app.exposition.dto.TransferDTO;
import com.acmebank.demo.bank.account.app.exposition.dto.WithdrawalDTO;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * Cucumber Steps with mocked server
 */
public class BankAccountStepDefs extends StepDefs {

	private static final String UNKNOWN = "FR9571471028571296961475535";

	public BankAccountStepDefs() {
		super();
	}

	public BankAccountStepDefs(final ResultActions actions) {
		super(actions);
	}

	@Autowired
	private BankAccountControler bankAccountResource;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private RestExceptionHandler restExceptionHandler;

	private ObjectMapper mapper;

	private String accountNumber1 = "";
	private String accountNumber2 = "";

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(bankAccountResource).setControllerAdvice(restExceptionHandler)
				.build();
		mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	@Given("^a bank account$")
	public void a_bank_account() throws Throwable {

		final AccountInfoDTO accountInfo = createNewAccount();
		accountNumber1 = accountInfo.getIban();
	}

	private AccountInfoDTO createNewAccount() throws UnsupportedEncodingException, Exception, JsonProcessingException,
	IOException, JsonParseException, JsonMappingException {
		final AccountingRequestDTO request = new AccountingRequestDTO();
		request.setLowerLimit(BigDecimal.valueOf(0));
		request.setCurrency(CurrencyEnum.fromValue("EUR"));

		final String result = mockMvc
				.perform(post("/api/accountings/").content(mapper.writeValueAsString(request))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();

		final AccountInfoDTO accountInfo = mapper.readValue(result, AccountInfoDTO.class);
		assertThat(accountInfo.getIban(), is(notNullValue()));
		return accountInfo;
	}

	private void initAccountBalance(final String accountNumber, final BigDecimal amount, final String currency)
			throws Exception, JsonProcessingException {
		final DepositDTO deposit = new DepositDTO();
		deposit.setAmount(amount);
		deposit.setCurrency(CurrencyEnum.fromValue(currency));

		mockMvc.perform(put("/api/accountings/" + accountNumber + "/deposit")
				.content(mapper.writeValueAsString(deposit)).contentType(MediaType.APPLICATION_JSON));
	}

	@Given("^initial balance is (\\d+) \"([^\"]*)\"$")
	public void initial_balance_is(final BigDecimal amount, final String currency) throws Throwable {
		initAccountBalance(accountNumber1, amount, currency);
	}

	@Given("^the initial balance of the other account is (\\d+) \"([^\"]*)\"$")
	public void the_initial_balance_of_the_other_account_is(final BigDecimal amount, final String currency)
			throws Throwable {
		initAccountBalance(accountNumber2, amount, currency);
	}

	@When("^User post a new account request with lower limit of (\\d+) \"([^\"]*)\"$")
	public void user_post_a_new_account_request_with_lower_limit_of(final BigDecimal lowerLimit, final String currency)
			throws Throwable {

		final AccountingRequestDTO request = new AccountingRequestDTO();
		request.setLowerLimit(lowerLimit);
		request.setCurrency(CurrencyEnum.fromValue(currency));

		actions = mockMvc.perform(post("/api/accountings/").content(mapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON));
	}

	@Then("^return a success status$")
	public void return_a_success_status() throws Throwable {
		actions.andExpect(status().is2xxSuccessful());
	}

	@Then("^Returned JSON object with account number is not null$")
	public void returned_JSON_object_with_account_number_is_not_null() throws Throwable {
		final AccountInfoDTO accountInfo = mapper.readValue(actions.andReturn().getResponse().getContentAsString(),
				AccountInfoDTO.class);

		assertThat(accountInfo.getIban(), is(notNullValue()));
	}

	@When("^a user makes a getBalance request for this account$")
	public void a_user_makes_a_getBalance_request_for_this_account() throws Throwable {

		actions = mockMvc
				.perform(get("/api/accountings/" + accountNumber1 + "/balance").accept(MediaType.APPLICATION_JSON));

	}

	@Then("^Returned JSON object with is (\\d+) \"([^\"]*)\"$")
	public void returned_JSON_object_with_is(final BigDecimal amount, final String currency) throws Throwable {

		final BalanceInfoDTO balanceInfo = mapper.readValue(actions.andReturn().getResponse().getContentAsString(),
				BalanceInfoDTO.class);

		assertThat(balanceInfo.getAmount(), is(notNullValue()));
		assertThat(balanceInfo.getAmount(), closeTo(amount, BigDecimal.ZERO));
		assertThat(balanceInfo.getCurrency(), is(notNullValue()));
		assertThat(balanceInfo.getCurrency(), equalTo(CurrencyEnum.fromValue(currency)));
	}

	@When("^a user makes a getBalance request for unknown account$")
	public void a_user_makes_a_getBalance_request_for_unknown_account() throws Throwable {

		actions = mockMvc.perform(get("/api/accountings/" + UNKNOWN + "/balance").accept(MediaType.APPLICATION_JSON));
	}

	@Then("^return a not found status$")
	public void return_a_not_found_status() throws Throwable {
		actions.andExpect(status().isNotFound())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	}

	@When("^a user makes a withdrawal of (\\d+) \"([^\"]*)\"$")
	public void a_user_makes_a_withdrawal_of(final BigDecimal amount, final String currency) throws Throwable {
		final WithdrawalDTO withdrawal = new WithdrawalDTO();
		withdrawal.setAmount(amount);
		withdrawal.setCurrency(CurrencyEnum.fromValue(currency));

		actions = mockMvc.perform(put("/api/accountings/" + accountNumber1 + "/withdrawal")
				.content(mapper.writeValueAsString(withdrawal)).contentType(MediaType.APPLICATION_JSON));

	}

	@When("^a user makes a withdrawal of (\\d+) \"([^\"]*)\" for unknown account$")
	public void a_user_makes_a_withdrawal_of_for_unknown_account(final BigDecimal amount, final String currency)
			throws Throwable {

		final WithdrawalDTO withdrawal = new WithdrawalDTO();
		withdrawal.setAmount(amount);
		withdrawal.setCurrency(CurrencyEnum.fromValue(currency));

		actions = mockMvc.perform(put("/api/accountings/" + UNKNOWN + "/withdrawal")
				.content(mapper.writeValueAsString(withdrawal)).contentType(MediaType.APPLICATION_JSON));
	}

	@When("^User makes a deposit of (\\d+) \"([^\"]*)\"$")
	public void user_makes_a_deposit_of(final BigDecimal amount, final String currency) throws Throwable {
		final DepositDTO deposit = new DepositDTO();
		deposit.setAmount(amount);
		deposit.setCurrency(CurrencyEnum.fromValue(currency));

		actions = mockMvc.perform(put("/api/accountings/" + accountNumber1 + "/deposit")
				.content(mapper.writeValueAsString(deposit)).contentType(MediaType.APPLICATION_JSON));
	}

	@When("^a user makes a deposit of (\\d+) \"([^\"]*)\" for unknown account$")
	public void a_user_makes_a_deposit_of_for_unknown_account(final BigDecimal amount, final String currency)
			throws Throwable {
		final DepositDTO deposit = new DepositDTO();
		deposit.setAmount(amount);
		deposit.setCurrency(CurrencyEnum.fromValue(currency));

		actions = mockMvc.perform(put("/api/accountings/" + UNKNOWN + "/deposit")
				.content(mapper.writeValueAsString(deposit)).contentType(MediaType.APPLICATION_JSON));
	}

	@Given("^a other bank account$")
	public void a_other_bank_account() throws Throwable {
		final AccountInfoDTO accountInfo = createNewAccount();
		accountNumber2 = accountInfo.getIban();
	}

	@When("^User makes a transfer of (\\d+) \"([^\"]*)\"$")
	public void user_makes_a_transfer_of(final BigDecimal amount, final String currency) throws Throwable {

		final TransferDTO transfer = new TransferDTO();
		transfer.setIbanTo(accountNumber2);
		transfer.setAmount(amount);
		transfer.setCurrency(CurrencyEnum.fromValue(currency));

		actions = mockMvc.perform(put("/api/accountings/" + accountNumber1 + "/transfer")
				.content(mapper.writeValueAsString(transfer)).contentType(MediaType.APPLICATION_JSON));
	}

	@Then("^the final balance of the account is (\\d+) \"([^\"]*)\"$")
	public void the_final_balance_of_the_account_is(final BigDecimal amount, final String currency) throws Throwable {
		final String result = mockMvc.perform(get("/api/accountings/" + accountNumber1 + "/balance")).andReturn()
				.getResponse().getContentAsString();

		final BalanceInfoDTO balanceInfo = mapper.readValue(result, BalanceInfoDTO.class);

		assertThat(balanceInfo.getAmount(), is(notNullValue()));
		assertThat(balanceInfo.getAmount(), closeTo(amount, BigDecimal.ZERO));
		assertThat(balanceInfo.getCurrency(), is(notNullValue()));
		assertThat(balanceInfo.getCurrency(), equalTo(CurrencyEnum.fromValue(currency)));
	}

	@Then("^the final balance of the other account is (\\d+) \"([^\"]*)\"$")
	public void the_final_balance_of_the_other_account_is(final BigDecimal amount, final String currency)
			throws Throwable {

		final String result = mockMvc.perform(get("/api/accountings/" + accountNumber2 + "/balance")).andReturn()
				.getResponse().getContentAsString();

		final BalanceInfoDTO balanceInfo = mapper.readValue(result, BalanceInfoDTO.class);

		assertThat(balanceInfo.getAmount(), is(notNullValue()));
		assertThat(balanceInfo.getAmount(), closeTo(amount, BigDecimal.ZERO));
		assertThat(balanceInfo.getCurrency(), is(notNullValue()));
		assertThat(balanceInfo.getCurrency(), equalTo(CurrencyEnum.fromValue(currency)));
	}

}
