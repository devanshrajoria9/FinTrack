package com.devansh.fintrack.service;

import com.devansh.fintrack.dto.response.CategoryExpenseResponseDto;
import com.devansh.fintrack.dto.response.DashboardSummaryResponseDto;
import com.devansh.fintrack.dto.response.MonthlySummaryResponseDto;
import com.devansh.fintrack.enums.TransactionType;
import com.devansh.fintrack.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DashboardService {

    private final TransactionRepository transactionRepository;

    public DashboardService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public DashboardSummaryResponseDto getSummary(){

        BigDecimal totalIncome =
                transactionRepository.getTotalAmountByType(TransactionType.INCOME);

        BigDecimal totalExpense =
                transactionRepository.getTotalAmountByType(TransactionType.EXPENSE);

        BigDecimal balance = totalIncome.subtract(totalExpense);

        DashboardSummaryResponseDto response = new DashboardSummaryResponseDto();

        response.setTotalIncome(totalIncome);
        response.setTotalExpense(totalExpense);
        response.setBalance(balance);

        return response;
    }

    public List<CategoryExpenseResponseDto> getCategoryWiseExpenses(){
        List<Object[]> results =
                transactionRepository.getCategoryWiseExpenses();

        return results.stream()
                .map(result -> {
                    CategoryExpenseResponseDto response = new CategoryExpenseResponseDto();

                    response.setCategory((String) result[0]);
                    response.setAmount((BigDecimal) result[1]);

                    return response;
                })
                .toList();
    }

    public List<MonthlySummaryResponseDto> getMonthlySummary(){
        List<Object[]> results =
                transactionRepository.getMonthlySummary();

        return results.stream()
                .map(result -> {

                    MonthlySummaryResponseDto response =
                            new MonthlySummaryResponseDto();

                    response.setMonth((String) result[0]);
                    response.setIncome((BigDecimal) result[1]);
                    response.setExpense((BigDecimal) result[2]);

                    return response;
                })
                .toList();
    }
}
