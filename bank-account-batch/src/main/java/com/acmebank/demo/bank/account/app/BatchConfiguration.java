package com.acmebank.demo.bank.account.app;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.SingleColumnRowMapper;

import com.acmebank.demo.bank.account.app.batch.config.ItemCountListener;
import com.acmebank.demo.bank.account.app.batch.export.BankAccountLineCsvDto;
import com.acmebank.demo.bank.account.app.domain.entity.BankAccount;
import com.acmebank.demo.bank.account.app.service.BankAccountServicePort;

/**
 * This is the Spring batch configuration class for "bankAccountExportJob"
 */
@Configuration
@EnableBatchProcessing
@Import(DataSourceAutoConfiguration.class)
@ComponentScan(basePackages = { "com.acmebank.demo.bank.account.app" })
@EnableJpaRepositories(basePackages = "com.acmebank.demo.bank.account.app.infrastructure")
public class BatchConfiguration {

	private final Logger logger = LoggerFactory.getLogger(BatchConfiguration.class);

	@Autowired
	private BankAccountServicePort service;

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Autowired
	public DataSource dataSource;

	/**
	 * ItemReader is an abstract representation of how data is provided as input to
	 * a Step. When the inputs are exhausted, the ItemReader returns null.
	 */
	@Bean
	public JdbcCursorItemReader<Long> reader() {
		final JdbcCursorItemReader<Long> reader = new JdbcCursorItemReader<Long>();
		reader.setDataSource(dataSource);
		reader.setSql("SELECT ID FROM BANK_ACCOUNT");
		reader.setRowMapper(new SingleColumnRowMapper<Long>());
		return reader;
	}

	/**
	 * ItemProcessor represents the business processing of an item. The data read by
	 * ItemReader can be passed on to ItemProcessor. In this unit, the data is
	 * transformed and sent for writing. If, while processing the item, it becomes
	 * invalid for further processing, you can return null. The nulls are not
	 * written by ItemWriter.
	 */
	@Bean
	public ItemProcessor<Long, BankAccountLineCsvDto> processor() {
		return new ItemProcessor<Long, BankAccountLineCsvDto>() {

			@Override
			public BankAccountLineCsvDto process(final Long id) throws Exception {
				return mapBankAccount2Dto(service.findOne(id));
			}

			private BankAccountLineCsvDto mapBankAccount2Dto(final BankAccount bankAccount) {
				final BankAccountLineCsvDto dto = new BankAccountLineCsvDto();
				dto.setAccountNumber(bankAccount.getAccountNumber().getValue());
				dto.setAmount(bankAccount.getBalance().getNumberStripped().toPlainString());
				dto.setCurrency(bankAccount.getAccountCurrency().getCurrencyCode());
				dto.setStatus(bankAccount.getStatus().name());
				return dto;
			}
		};
	}

	/**
	 * ItemWriter is the output of a Step. The writer writes one batch or chunk of
	 * items at a time to the target system. ItemWriter has no knowledge of the
	 * input it will receive next, only the item that was passed in its current
	 * invocation.
	 */
	@Bean
	@StepScope
	public FlatFileItemWriter<BankAccountLineCsvDto> writer(
			@Value("#{jobParameters['bankAccounts.file']}") final String csvFile) {
		final FlatFileItemWriter<BankAccountLineCsvDto> writer = new FlatFileItemWriter<BankAccountLineCsvDto>();
		writer.setResource(new FileSystemResource(csvFile));
		writer.setLineAggregator(new DelimitedLineAggregator<BankAccountLineCsvDto>() {
			{
				setDelimiter(";");
				setFieldExtractor(new BeanWrapperFieldExtractor<BankAccountLineCsvDto>() {
					{
						setNames(
								new String[] { "accountNumber", "currency", "amount", "status" });
					}
				});
			}
		});
		return writer;
	}

	/**
	 * Used to log job progression
	 */
	@Bean
	public ItemCountListener listener() {
		final ItemCountListener listener = new ItemCountListener();
		listener.setItemName("bank accounts");
		listener.setLoggingInterval(5); // Log process item count every 5
		return listener;
	}

	/**
	 * Step builder
	 *
	 * @return the step
	 */
	@Bean
	public Step exportStep() {
		return stepBuilderFactory.get("export-step") //
				.<Long, BankAccountLineCsvDto>chunk(1) //
				.reader(reader()) //
				.processor(processor()) //
				.writer(writer(null)) //
				.listener(listener()) //
				.build();
	}

	/**
	 * Job Builder
	 *
	 * @return the job
	 */
	@Bean(name = "bankAccountExportJob")
	public Job exportJob() {
		logger.info("creating bankAccountExportJob");
		return jobBuilderFactory.get("bankAccountExportJob") //
				.incrementer(new RunIdIncrementer()) //
				.flow(exportStep()) //
				.end().build();
	}

}
