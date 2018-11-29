package com.acmebank.demo.bank.account.app.exposition.api.step;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import com.acmebank.demo.bank.account.app.exposition.BankAccountApplication;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = BankAccountApplication.class)
@AutoConfigureMockMvc
public abstract class StepDefs {

	protected ResultActions actions;

	public StepDefs() {
		//
	}

	public StepDefs(final ResultActions actions) {
		this.actions = actions;
	}

	// this step should be be factorized for few scenarios

}
