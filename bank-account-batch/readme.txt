@echo off


java -jar target/bank-account-batch-0.1-SNAPSHOT-spring-boot.jar com.acmebank.demo.bank.account.app.BatchConfiguration bankAccountExportJob bankAccounts.file=accounts.csv
