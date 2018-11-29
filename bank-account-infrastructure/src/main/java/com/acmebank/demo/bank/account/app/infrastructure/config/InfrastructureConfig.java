package com.acmebank.demo.bank.account.app.infrastructure.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootConfiguration
@ComponentScan(basePackages = { "com.acmebank.demo.bank.account.app" })
@EnableJpaRepositories
public class InfrastructureConfig /* extends HibernateJpaAutoConfiguration */ {

	// public InfrastructureConfig(final DataSource dataSource, final JpaProperties
	// jpaProperties,
	// final ObjectProvider<JtaTransactionManager> jtaTransactionManager,
	// final ObjectProvider<TransactionManagerCustomizers>
	// transactionManagerCustomizers) {
	// super();
	// }
	//
	// @Bean
	// public LocalContainerEntityManagerFactoryBean entityManagerFactory(
	// final EntityManagerFactoryBuilder factoryBuilder) {
	// final LocalContainerEntityManagerFactoryBean ret =
	// super.entityManagerFactory(factoryBuilder);
	// ret.setMappingResources("META-INF/orm.xml");
	// return ret;
	// }
}