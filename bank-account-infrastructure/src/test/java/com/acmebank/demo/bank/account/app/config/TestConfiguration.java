package com.acmebank.demo.bank.account.app.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootConfiguration
//// @AutoConfigurationPackage
@ComponentScan(basePackages = { "com.acmebank.demo.bank.account.app" })
@EnableJpaRepositories("com.acmebank.demo.bank.account.app.infrastructure")
public class TestConfiguration {

	public static void main(final String[] args) {
		SpringApplication.run(TestConfiguration.class, args);
	}

}