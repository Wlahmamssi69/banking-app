package com.example.ebank;
import com.example.ebank.entities.*;
import com.example.ebank.enums.AccountStatus;
import com.example.ebank.enums.OperationType;
import com.example.ebank.exceptions.BalanceNotSufficientException;
import com.example.ebank.repositories.BankAccountRepository;
import com.example.ebank.repositories.CustomerRepository;
import com.example.ebank.repositories.OperationRepository;
import com.example.ebank.service.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

@SpringBootApplication
public class EbankApplication {


    public static void main(String[] args) {
        SpringApplication.run(EbankApplication.class, args);
    }

    @Bean
    CommandLineRunner start(
            BankAccountService bankAccountService,
            BankAccountRepository accountRepository
    ){
        return args-> {

            Stream.of("Ahmed", "Mohamed", "Douaa").forEach(
                    name -> {
                        Customer customer = new Customer();
                        customer.setFullName(name);
                        customer.setEmail(name + "@gmail.com");
                        bankAccountService.saveCustomer(customer);
                    }
            );


            bankAccountService.customersList().forEach(customer -> {

                        bankAccountService.saveSavingAccount(Math.random() * 9000, 2.5, customer.getId());
                        bankAccountService.saveCurrentAccount(Math.random() * 90000, 8000.0, customer.getId());
                    });



            accountRepository.findAll().forEach(account->{
                try {
                    bankAccountService.debit(Math.random()*900,account.getId());
                    bankAccountService.credit(Math.random()*900, account.getId());

                } catch (BalanceNotSufficientException e) {
                    throw new RuntimeException(e);
                }
            });



    };
    }}
