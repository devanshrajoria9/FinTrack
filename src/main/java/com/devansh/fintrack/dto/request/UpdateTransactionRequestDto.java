package com.devansh.fintrack.dto.request;

import com.devansh.fintrack.enums.TransactionType;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
public class UpdateTransactionRequestDto {
    @NotBlank(message = "Transaction title is required")
    @Size(min = 3, max = 100 , message = "Transaction title must be between 3 and 100 characters")
    private String title;
    @NotNull(message = "Transaction amount is required")
    @DecimalMin(value = "0.01",
            message = "Transaction amount must be greater than 0")
    private BigDecimal amount;
    @Size(max = 255, message = "Description cannot exceed 255 characters")
    private String description;
    @NotNull(message = "Transaction type is required")
    private TransactionType type;
    @NotNull(message = "Category Id is required")
    private Long categoryId;
}
