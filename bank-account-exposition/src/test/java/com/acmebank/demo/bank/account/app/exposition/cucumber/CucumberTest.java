package com.acmebank.demo.bank.account.app.exposition.cucumber;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(strict = true, monochrome = false, glue = "com.acmebank.demo.bank.account.app.exposition.api.step", features = "src/test/resources/features/BankAccount.feature", plugin = {
		"pretty", "html:target/cucumber", "json:target/cucumber/cucumber.json" })
public class CucumberTest {

}
