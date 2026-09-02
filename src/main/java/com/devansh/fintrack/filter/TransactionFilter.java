package com.devansh.fintrack.filter;

import com.devansh.fintrack.enums.TransactionType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
public class TransactionFilter {
private LocalDate startDate;
private LocalDate endDate;
private TransactionType type;
private Long categoryId;
private BigDecimal minAmount;
private BigDecimal maxAmount;
private String search;
}
