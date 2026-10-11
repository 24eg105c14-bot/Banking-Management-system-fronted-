package com.bank.service;

import com.bank.model.BankTransaction;
import com.bank.model.Customer;
import com.bank.repository.CustomerRepository;
import com.bank.repository.TransactionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionService {
    private final CustomerRepository customers;
    private final TransactionRepository transactions;
    public TransactionService(CustomerRepository customers,TransactionRepository transactions){this.customers=customers;this.transactions=transactions;}

    @Transactional
    public BankTransaction transact(Long customerId,BigDecimal amount,boolean deposit){
        if(amount==null || amount.compareTo(BigDecimal.ZERO)<=0 || amount.scale()>2)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Amount must be positive and have at most 2 decimal places.");
        Customer c=customers.findById(customerId).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Customer not found."));
        BigDecimal updated;
        if(deposit) updated=c.getBalance().add(amount);
        else {
            if(c.getBalance().compareTo(amount)<0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Insufficient balance.");
            updated=c.getBalance().subtract(amount);
        }
        c.setBalance(updated); customers.save(c);
        return transactions.save(new BankTransaction(customerId,deposit?"DEPOSIT":"WITHDRAWAL",amount));
    }
    public List<BankTransaction> history(Long customerId){
        if(!customers.existsById(customerId)) throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Customer not found.");
        return transactions.findByCustomerIdOrderByCreatedAtDesc(customerId);
    }
}