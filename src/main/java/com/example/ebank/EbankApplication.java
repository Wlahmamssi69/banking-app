package com.example.ebank;

import com.example.ebank.entities.*;
import com.example.ebank.enums.AccountStatus;
import com.example.ebank.enums.OperationType;
import com.example.ebank.repositories.BankAccountRepository;
import com.example.ebank.repositories.CustomerRepository;
import com.example.ebank.repositories.OperationRepository;
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
            CustomerRepository customerRepository,
            BankAccountRepository accountRepository,
            OperationRepository operationRepository
    ){
        return args->{

            Stream.of("Ahmed","Mohamed","Douaa").forEach(
                    name->{
                        Customer customer= new Customer();
                        customer.setFullName(name);
                        customer.setEmail(name+"@gmail.com");
                        customerRepository.save(customer);
                    }
            );

            customerRepository.findAll().forEach(customer -> {

                SavingAccount SA=new SavingAccount();
                SA.setBalance(Math.random()*90000);
                SA.setCustomer(customer);
                SA.setCurrency("MAD");
                SA.setAccountStatus(AccountStatus.CREATED);
                SA.setCreatedAt(new Date());
                SA.setInterestRate(2.5);
                accountRepository.save(SA);


                CurrentAccount CA=new CurrentAccount();
                CA.setBalance(Math.random()*90000);
                CA.setCustomer(customer);
                CA.setCreatedAt(new Date());
                CA.setAccountStatus(AccountStatus.CREATED);
                CA.setCurrency("MAD");
                CA.setOverDraft(Math.random()*9000);
                accountRepository.save(CA);

            });


            accountRepository.findAll().forEach(account->{

                operationRepository.saveAll(
                        List.of(
                                Operation.builder()
                                        .account(account)
                                        .OperationType(OperationType.DEBIT)
                                        .amount(Math.random()*900)
                                        .operationDate(new Date())
                                        .build(),
                                Operation.builder()
                                        .account(account)
                                        .OperationType(OperationType.CREDIT)
                                        .amount(Math.random()*900)
                                        .operationDate(new Date())
                                        .build()
                        )



                );
            });



        };
    }
}
