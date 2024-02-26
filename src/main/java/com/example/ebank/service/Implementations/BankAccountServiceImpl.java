package com.example.ebank.service.Implementations;
import com.example.ebank.entities.*;
import com.example.ebank.enums.AccountStatus;
import com.example.ebank.enums.OperationType;
import com.example.ebank.exceptions.BalanceNotSufficientException;
import com.example.ebank.exceptions.BankAccountNotFoundException;
import com.example.ebank.exceptions.CustomerNotFoundException;
import com.example.ebank.repositories.BankAccountRepository;
import com.example.ebank.repositories.CustomerRepository;
import com.example.ebank.repositories.OperationRepository;
import com.example.ebank.service.BankAccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
@Slf4j

public class BankAccountServiceImpl implements BankAccountService {
    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private OperationRepository operationRepository;


    @Override
    public Customer saveCustomer(Customer customer) {
        log.info("creating a customer");
        Customer savedCustomer=customerRepository.save(customer);
        return savedCustomer;
    }
    @Override
    public CurrentAccount saveCurrentAccount(double initialBalance, double overDraft, long customerid) {
        log.info("Creating a current account");
        CurrentAccount savedAccount=new CurrentAccount();
        Customer customer=customerRepository.findById(customerid).orElse(null);
        if(customer==null){
            throw new CustomerNotFoundException("Customer with id:"+customerid+"is not found");
        }
        savedAccount.setBalance(initialBalance);
        savedAccount.setAccountStatus(AccountStatus.CREATED);
        savedAccount.setCreatedAt(new Date());
        savedAccount.setCustomer(customer);
        savedAccount.setOverDraft(overDraft);
        bankAccountRepository.save(savedAccount);
        return savedAccount;
    }

    @Override
    public SavingAccount saveSavingAccount(double initialBalance, double interestRate, long customerid) {
        log.info("Creating a saving account");

        SavingAccount savedAccount=new SavingAccount();

        Customer customer=customerRepository.findById(customerid).orElse(null);
        if(customer==null){
            throw new CustomerNotFoundException("Customer with id:"+customerid+"is not found");
        }
        savedAccount.setBalance(initialBalance);
        savedAccount.setAccountStatus(AccountStatus.CREATED);
        savedAccount.setCreatedAt(new Date());
        savedAccount.setCustomer(customer);
        savedAccount.setInterestRate(interestRate);
        bankAccountRepository.save(savedAccount);
        return savedAccount;
    }


    @Override
    public List<Customer> customersList() {

        log.info("retrieve customers list");

        return customerRepository.findAll();
    }

    @Override
    public BankAccount getBankAccount(long accountId) throws BankAccountNotFoundException{
        log.info("get account by id");

        return bankAccountRepository.findById(accountId)
                .orElseThrow( ()-> {throw new BankAccountNotFoundException("Bank Account not found");});
    }

    @Override
    public void debit(double amount, long accountId)  {
        log.info("debit an ammount from an account");

        BankAccount bankAccount= getBankAccount(accountId);

        Operation operation=new Operation();
        operation.setOperationType(OperationType.DEBIT);
        operation.setOperationDate(new Date());
        operation.setAmount(amount);
        operation.setDescription("Debit "+amount+"from account with id "+accountId);
        operation.setAccount(bankAccount);
        operationRepository.save(operation);

        bankAccount.getOperationList().add(operation);
        bankAccount.setBalance(bankAccount.getBalance()+amount);
        bankAccountRepository.save(bankAccount);

    }

    @Override
    public void credit(double amount, long accountId) throws BalanceNotSufficientException {
        log.info("Credit an amount from an account");

        BankAccount bankAccount= getBankAccount(accountId);

        if(bankAccount.getBalance()<amount) throw new BalanceNotSufficientException("Balance not sufficient");

        Operation operation=new Operation();
        operation.setOperationType(OperationType.CREDIT);
        operation.setOperationDate(new Date());
        operation.setAmount(amount);
        operation.setDescription("Credit "+amount+"from account with id "+accountId);
        operation.setAccount(bankAccount);
        operationRepository.save(operation);

        bankAccount.getOperationList().add(operation);
        bankAccount.setBalance(bankAccount.getBalance()-amount);
        bankAccountRepository.save(bankAccount);
}


    @Override
    public void tranfer(double ammount, long sourceId, long destId) throws BalanceNotSufficientException {
        log.info("transfer an amount to an account ");

    debit(ammount,destId);
    credit(ammount,sourceId);
    }
}
