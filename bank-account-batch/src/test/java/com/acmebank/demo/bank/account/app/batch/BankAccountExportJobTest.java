package com.acmebank.demo.bank.account.app.batch;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.test.AssertFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.batch.JobLauncherCommandLineRunner;
import org.springframework.core.io.FileSystemResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.acmebank.demo.bank.account.app.BatchApplication;
import com.acmebank.demo.bank.account.app.BatchConfiguration;

/**
 * End-To-End test job "export bank account to csv file"
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { BatchApplication.class, BatchConfiguration.class, })
public class BankAccountExportJobTest {
	private static final Logger log = LoggerFactory.getLogger(BankAccountExportJobTest.class);

	private static final String EXPECTED_FILE = "src/test/resources/data/accounts.txt";
	private static final String OUTPUT_FILE = "target/test-outputs/accounts.txt";

	@Autowired
	private JobLauncherCommandLineRunner runner;

	@Before
	public void setup() throws Exception {
		log.debug("erase previous output file");
		FileUtils.deleteQuietly(new File(OUTPUT_FILE));
	}

	@Test
	public void launchBankAccountExportJobTest() throws Exception {
		log.debug("launch ExportJob");
		final String[] myArgs = new String[] { "bankAccounts.file= " + OUTPUT_FILE };
		runner.run(myArgs);

		// then check results
		assertTrue(new File(OUTPUT_FILE).exists());
		AssertFile.assertFileEquals(new FileSystemResource(EXPECTED_FILE), new FileSystemResource(OUTPUT_FILE));
	}
}