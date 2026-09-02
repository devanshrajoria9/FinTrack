package com.devansh.fintrack.repository;

import com.devansh.fintrack.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TransactionRepository
            extends JpaRepository<Transaction, Long>,
            JpaSpecificationExecutor<Transaction> {
}
