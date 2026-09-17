package com.devansh.fintrack.repository;

import com.devansh.fintrack.entity.Transaction;
import com.devansh.fintrack.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionRepository
            extends JpaRepository<Transaction, Long>,
            JpaSpecificationExecutor<Transaction> {

    @Query("""
            SELECT COALESCE(SUM(t.amount), 0)
            FROM Transaction t
            WHERE t.type = :type
            """)
    BigDecimal getTotalAmountByType(
            @Param("type") TransactionType type
    );

    @Query("""
        SELECT t.category.name, SUM(t.amount)
        FROM Transaction t
        WHERE t.type = com.devansh.fintrack.enums.TransactionType.EXPENSE
        GROUP BY t.category.name
        """)
    List<Object[]> getCategoryWiseExpenses();

    @Query("""
        SELECT
            FUNCTION('DATE_FORMAT', t.transactionDate, '%Y-%m'),
            SUM(CASE
                    WHEN t.type = com.devansh.fintrack.enums.TransactionType.INCOME
                    THEN t.amount
                    ELSE 0
                END),
            SUM(CASE
                    WHEN t.type = com.devansh.fintrack.enums.TransactionType.EXPENSE
                    THEN t.amount
                    ELSE 0
                END)
        FROM Transaction t
        GROUP BY FUNCTION('DATE_FORMAT', t.transactionDate, '%Y-%m')
        ORDER BY FUNCTION('DATE_FORMAT', t.transactionDate, '%Y-%m')
        """)
    List<Object[]> getMonthlySummary();
}
