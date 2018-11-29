package api.cucumber;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(strict = true, monochrome = false, glue = "step.definitions", features = "src/test/resources/features/BankAccount.feature", plugin = {
        "pretty", "junit:target/cucumber-results.xml", "html:target/cucumber", "json:target/cucumber/cucumber.json" })

public class CucumberTest {

}
