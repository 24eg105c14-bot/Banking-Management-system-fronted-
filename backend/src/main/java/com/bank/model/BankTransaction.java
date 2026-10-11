package com.bank.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bank_transactions")
public class BankTransaction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false) private Long customerId;
    @Column(nullable=false, length=20) private String type;
    @Column(nullable=false, precision=14, scale=2) private BigDecimal amount;
    @Column(nullable=false) private LocalDateTime createdAt = LocalDateTime.now();

    public BankTransaction() {}
    public BankTransaction(Long customerId,String type,BigDecimal amount){this.customerId=customerId;this.type=type;this.amount=amount;this.createdAt=LocalDateTime.now();}
    public Long getId(){return id;} public Long getCustomerId(){return customerId;} public void setCustomerId(Long v){customerId=v;}
    public String getType(){return type;} public void setType(String v){type=v;}
    public BigDecimal getAmount(){return amount;} public void setAmount(BigDecimal v){amount=v;}
    public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime v){createdAt=v;}
}