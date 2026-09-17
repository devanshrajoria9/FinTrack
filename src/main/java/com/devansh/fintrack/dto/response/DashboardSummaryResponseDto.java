package com.devansh.fintrack.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class DashboardSummaryResponseDto {

    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal balance;
}
