package com.devansh.fintrack.repository;

import com.devansh.fintrack.entity.Transaction;
import com.devansh.fintrack.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository
            extends JpaRepository<Transaction, Long>,
            JpaSpecificationExecutor<Transaction> {

    Page<Transaction> findAllByUserId(Long userId, Pageable pageable);

    Optional<Transaction> findByIdAndUserId(Long id, Long userId);


    @Query("""
            SELECT COALESCE(SUM(t.amount), 0)
            FROM Transaction t
            WHERE t.type = :type
            AND t.user.id = :userId
            """)
    BigDecimal getTotalAmountByType(
            @Param("type") TransactionType type,
            @Param("userId") Long userId
    );

    @Query("""
        SELECT t.category.name, SUM(t.amount)
        FROM Transaction t
        WHERE t.type = com.devansh.fintrack.enums.TransactionType.EXPENSE
        AND t.user.id = :userId
        GROUP BY t.category.name
        """)
    List<Object[]> getCategoryWiseExpenses(
            @Param("userId") Long userId);

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
        WHERE t.user.id = :userId
        GROUP BY FUNCTION('DATE_FORMAT', t.transactionDate, '%Y-%m')
        ORDER BY FUNCTION('DATE_FORMAT', t.transactionDate, '%Y-%m')
        """)
    List<Object[]> getMonthlySummary(
            @Param("userId") Long userId);
}
