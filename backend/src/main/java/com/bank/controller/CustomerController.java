package com.bank.controller;

import com.bank.model.Customer;
import com.bank.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerService service;
    public CustomerController(CustomerService service){this.service=service;}

    record RegisterRequest(String name,String email,String password){}
    record LoginRequest(String email,String password){}
    record CustomerResponse(Long id,String name,String email,String accountNumber,BigDecimal balance){}

    private CustomerResponse response(Customer c){return new CustomerResponse(c.getId(),c.getName(),c.getEmail(),c.getAccountNumber(),c.getBalance());}

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponse register(@RequestBody RegisterRequest req){return response(service.register(req.name(),req.email(),req.password()));}
    @PostMapping("/login")
    public CustomerResponse login(@RequestBody LoginRequest req){return response(service.login(req.email(),req.password()));}
    @GetMapping("/{id}")
    public CustomerResponse get(@PathVariable Long id){return response(service.get(id));}
}