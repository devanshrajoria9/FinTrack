package com.devansh.fintrack.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
@Setter
@Getter
@NoArgsConstructor
public class CategoryExpenseResponseDto {

    private String category;
    private BigDecimal amount;
}
