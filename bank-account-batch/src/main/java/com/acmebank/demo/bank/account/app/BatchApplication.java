package com.acmebank.demo.bank.account.app;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
@EnableAutoConfiguration
public class BatchApplication {

	/**
	 * Tell to spring that this is not a Web application
	 */
	public static void main(final String[] args) throws Exception {
		new SpringApplicationBuilder(BatchApplication.class).web(WebApplicationType.NONE).run(args);
	}

}