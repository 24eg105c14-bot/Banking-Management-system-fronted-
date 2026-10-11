package com.bank.service;

import com.bank.model.Customer;
import com.bank.repository.CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;
import java.util.UUID;

@Service
public class CustomerService {
    private final CustomerRepository repo;
    private final PasswordEncoder encoder;
    public CustomerService(CustomerRepository repo, PasswordEncoder encoder){this.repo=repo;this.encoder=encoder;}

    public Customer register(String name,String email,String password){
        if(name==null || name.isBlank() || email==null || email.isBlank() || password==null || password.length()<8)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Name, email and password (8+ characters) are required.");
        if(repo.existsByEmail(email.trim().toLowerCase())) throw new ResponseStatusException(HttpStatus.CONFLICT,"Email is already registered.");
        Customer c=new Customer(); c.setName(name.trim()); c.setEmail(email.trim().toLowerCase());
        c.setPassword(encoder.encode(password)); c.setAccountNumber("AC"+UUID.randomUUID().toString().replace("-","").substring(0,12).toUpperCase());
        c.setBalance(new BigDecimal("0.00")); return repo.save(c);
    }
    public Customer login(String email,String password){
        Customer c=repo.findByEmail(email==null?"":email.trim().toLowerCase()).orElseThrow(()->new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid email or password."));
        if(password==null || !encoder.matches(password,c.getPassword())) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid email or password.");
        return c;
    }
    public Customer get(Long id){return repo.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Customer not found."));}
}