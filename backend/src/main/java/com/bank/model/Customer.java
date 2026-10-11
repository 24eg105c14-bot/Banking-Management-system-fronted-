package com.bank.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "customers")
public class Customer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false, length=100) private String name;
    @Column(nullable=false, unique=true, length=160) private String email;
    @Column(nullable=false) private String password;
    @Column(nullable=false, unique=true, length=24) private String accountNumber;
    @Column(nullable=false, precision=14, scale=2) private BigDecimal balance = BigDecimal.ZERO;

    public Customer() {}
    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public String getName(){return name;} public void setName(String name){this.name=name;}
    public String getEmail(){return email;} public void setEmail(String email){this.email=email;}
    public String getPassword(){return password;} public void setPassword(String password){this.password=password;}
    public String getAccountNumber(){return accountNumber;} public void setAccountNumber(String accountNumber){this.accountNumber=accountNumber;}
    public BigDecimal getBalance(){return balance;} public void setBalance(BigDecimal balance){this.balance=balance;}
}