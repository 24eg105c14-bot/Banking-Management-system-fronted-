package com.bank.repository;
import com.bank.model.BankTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface TransactionRepository extends JpaRepository<BankTransaction,Long> {
    List<BankTransaction> findByCustomerIdOrderByCreatedAtDesc(Long customerId);
}