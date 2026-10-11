package com.bank.controller;

import com.bank.model.BankTransaction;
import com.bank.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionService service;
    public TransactionController(TransactionService service){this.service=service;}
    record TransactionRequest(Long customerId,BigDecimal amount){}
    @PostMapping("/deposit")
    @ResponseStatus(HttpStatus.CREATED)
    public BankTransaction deposit(@RequestBody TransactionRequest r){return service.transact(r.customerId(),r.amount(),true);}
    @PostMapping("/withdraw")
    @ResponseStatus(HttpStatus.CREATED)
    public BankTransaction withdraw(@RequestBody TransactionRequest r){return service.transact(r.customerId(),r.amount(),false);}
    @GetMapping("/customer/{customerId}")
    public List<BankTransaction> history(@PathVariable Long customerId){return service.history(customerId);}
}