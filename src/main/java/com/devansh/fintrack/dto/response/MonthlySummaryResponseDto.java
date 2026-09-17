package com.devansh.fintrack.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
public class MonthlySummaryResponseDto {

    private String month;
    private BigDecimal income;
    private BigDecimal expense;
}
