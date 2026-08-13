package com.devansh.fintrack.dto.response;

import com.devansh.fintrack.enums.TransactionType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
public class TransactionResponseDto {
    private Long id;
    private String title;
    private BigDecimal amount;
    private String description;
    private LocalDateTime transactionDate;
    private TransactionType type;
    private Long userId;
    private Long categoryId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
