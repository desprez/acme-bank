package step.definitions;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.slf4j.LoggerFactory.getLogger;

import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.slf4j.Logger;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

public class BankAccountStepsDefinition {

    private static final Logger logger = getLogger(BankAccountStepsDefinition.class);

    private String hostEndpoint = "";

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;

    private static final String UNKNOWN = "FR9571471028571296961475535";
    private String accountNumber1 = "";
    private String accountNumber2 = "";

    @Before
    public void setup() {
        hostEndpoint = System.getProperty("host.endpoint");
        if (hostEndpoint == "") {
            System.out.println("[host.endpoint] system property is not defined");
            System.out.println("You must define a system property in VM arguments");
            System.out.println(" ex : -Dhost.endpoint=http://localhost:8080/T1/api/accountings/");
        }
    }

    @After
    public void teardown() {
        RestAssured.reset();
    }

    @When("^User post a new account request with lower limit of (\\d+) \"([^\"]*)\"$")
    public void user_post_a_new_account_request_with_lower_limit_of(final Integer amount, final String currency)
            throws Throwable {

        final JSONObject jsonObj = new JSONObject() //
                .put("lowerLimit", amount) //
                .put("currency", currency);

        request = given().contentType("application/json").body(jsonObj.toString());
        response = request.when().post(hostEndpoint);
    }

    @Then("^return a success status$")
    public void return_a_success_status() throws Throwable {
        json = response.then().assertThat().statusCode(anyOf(is(HttpStatus.SC_OK), is(HttpStatus.SC_CREATED)));
    }

    @Then("^Returned JSON object with account number is not null$")
    public void returned_JSON_object_with_account_number_is_not_null() throws Throwable {
        json.body("iban", is(notNullValue()));
    }

    @Given("^a bank account$")
    public void a_bank_account() throws Throwable {
        createBankAccount();
        accountNumber1 = response.path("iban").toString();
    }

    private void createBankAccount() throws JSONException {
        final JSONObject jsonObj = new JSONObject() //
                .put("lowerLimit", 0) //
                .put("currency", "EUR");

        request = given().contentType("application/json").body(jsonObj.toString());

        response = request.when().post(hostEndpoint);
        json = response.then().assertThat().statusCode(equalTo(HttpStatus.SC_CREATED));
        logger.debug(json.toString());
    }

    @Given("^initial balance is (\\d+) \"([^\"]*)\"$")
    public void initial_balance_is(final Integer amount, final String currency) throws Throwable {
        final JSONObject jsonObj = new JSONObject() //
                .put("amount", amount) //
                .put("currency", currency);

        request = given().contentType("application/json").body(jsonObj.toString());

        response = request.when().put(hostEndpoint + accountNumber1 + "/deposit");
        json = response.then().assertThat().statusCode(equalTo(HttpStatus.SC_OK));
    }

    @When("^a user makes a getBalance request for this account$")
    public void a_user_makes_a_getBalance_request_for_this_account() throws Throwable {
        request = given();
        response = request.when().get(hostEndpoint + accountNumber1 + "/balance");
    }

    @Then("^Returned JSON object with is (\\d+) \"([^\"]*)\"$")
    public void returned_JSON_object_with_is(final Integer amount, final String currency) throws Throwable {
        json.body("amount", equalTo(amount));
        json.body("currency", equalTo(currency));
    }

    @When("^a user makes a getBalance request for unknown account$")
    public void a_user_makes_a_getBalance_request_for_unknown_account() throws Throwable {
        request = given();
        response = request.when().get(hostEndpoint + UNKNOWN + "/balance");
    }

    @Then("^return a not found status$")
    public void return_a_not_found_status() throws Throwable {
        json = response.then().assertThat().statusCode(equalTo(HttpStatus.SC_NOT_FOUND));
    }

    @When("^a user makes a withdrawal of (\\d+) \"([^\"]*)\"$")
    public void a_user_makes_a_withdrawal_of(final Integer amount, final String currency) throws Throwable {
        final JSONObject jsonObj = new JSONObject() //
                .put("amount", amount) //
                .put("currency", currency);

        request = given().contentType("application/json").body(jsonObj.toString());
        response = request.when().put(hostEndpoint + accountNumber1 + "/withdrawal");
    }

    @Then("^the final balance of the account is (\\d+) \"([^\"]*)\"$")
    public void the_final_balance_of_the_account_is(final Integer amount, final String currency) throws Throwable {
        request = given();
        response = request.when().get(hostEndpoint + accountNumber1 + "/balance");
        json = response.then();
        json.body("amount", equalTo(amount));
        json.body("currency", equalTo(currency));
    }

    @When("^a user makes a withdrawal of (\\d+) \"([^\"]*)\" for unknown account$")
    public void a_user_makes_a_withdrawal_of_for_unknown_account(final Integer amount, final String currency)
            throws Throwable {
        final JSONObject jsonObj = new JSONObject() //
                .put("amount", amount) //
                .put("currency", currency);

        request = given().contentType("application/json").body(jsonObj.toString());
        response = request.when().put(hostEndpoint + UNKNOWN + "/withdrawal");
    }

    @When("^User makes a deposit of (\\d+) \"([^\"]*)\"$")
    public void user_makes_a_deposit_of(final Integer amount, final String currency) throws Throwable {
        final JSONObject jsonObj = new JSONObject() //
                .put("amount", amount) //
                .put("currency", currency);

        request = given().contentType("application/json").body(jsonObj.toString());
        response = request.when().put(hostEndpoint + accountNumber1 + "/deposit");
    }

    @When("^a user makes a deposit of (\\d+) \"([^\"]*)\" for unknown account$")
    public void a_user_makes_a_deposit_of_for_unknown_account(final Integer amount, final String currency)
            throws Throwable {
        final JSONObject jsonObj = new JSONObject() //
                .put("amount", amount) //
                .put("currency", currency);

        request = given().contentType("application/json").body(jsonObj.toString());
        response = request.when().put(hostEndpoint + UNKNOWN + "/deposit");
    }

    @Given("^a other bank account$")
    public void a_other_bank_account() throws Throwable {
        createBankAccount();

        accountNumber2 = response.path("iban").toString();
    }

    @Given("^the initial balance of the other account is (\\d+) \"([^\"]*)\"$")
    public void the_initial_balance_of_the_other_account_is(final Integer amount, final String currency)
            throws Throwable {

        final JSONObject jsonObj = new JSONObject() //
                .put("amount", amount) //
                .put("currency", currency);

        request = given().contentType("application/json").body(jsonObj.toString());

        response = request.when().put(hostEndpoint + accountNumber2 + "/deposit");
        json = response.then().assertThat().statusCode(equalTo(HttpStatus.SC_OK));
    }

    @When("^User makes a transfer of (\\d+) \"([^\"]*)\"$")
    public void user_makes_a_transfer_of(final Integer amount, final String currency) throws Throwable {
        final JSONObject jsonObj = new JSONObject() //
                .put("amount", amount) //
                .put("currency", currency) //
                .put("ibanTo", accountNumber2);

        request = given().contentType("application/json").body(jsonObj.toString());
        response = request.when().put(hostEndpoint + accountNumber1 + "/transfer");
    }

    @Then("^the final balance of the other account is (\\d+) \"([^\"]*)\"$")
    public void the_final_balance_of_the_other_account_is(final Integer amount, final String currency)
            throws Throwable {
        request = given();
        response = request.when().get(hostEndpoint + accountNumber2 + "/balance");
        json = response.then();
        json.body("amount", equalTo(amount));
        json.body("currency", equalTo(currency));
    }

}
