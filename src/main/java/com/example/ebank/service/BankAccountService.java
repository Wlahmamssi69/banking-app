package com.example.ebank.service;
import com.example.ebank.entities.BankAccount;
import com.example.ebank.entities.CurrentAccount;
import com.example.ebank.entities.Customer;
import com.example.ebank.entities.SavingAccount;
import com.example.ebank.exceptions.BalanceNotSufficientException;
import com.example.ebank.exceptions.BankAccountNotFoundException;
import java.util.List;


public interface BankAccountService {
    Customer saveCustomer(Customer customer);
    CurrentAccount saveCurrentAccount(double initialBalance, double overDraft, long customerid);
    SavingAccount saveSavingAccount(double initialBalance, double interestRate, long customerid);

    List<Customer> customersList();
    BankAccount getBankAccount(long accountId) throws BankAccountNotFoundException;
    void debit(double ammount,long accountId) throws BalanceNotSufficientException;
    void credit(double ammount,long accountId) throws BalanceNotSufficientException;
    void tranfer(double ammount,long sourceId, long destId) throws BalanceNotSufficientException;


}
