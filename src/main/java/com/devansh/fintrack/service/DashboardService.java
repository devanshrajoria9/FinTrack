package com.devansh.fintrack.service;

import com.devansh.fintrack.dto.response.CategoryExpenseResponseDto;
import com.devansh.fintrack.dto.response.DashboardSummaryResponseDto;
import com.devansh.fintrack.dto.response.MonthlySummaryResponseDto;
import com.devansh.fintrack.entity.User;
import com.devansh.fintrack.enums.TransactionType;
import com.devansh.fintrack.exception.ResourceNotFoundException;
import com.devansh.fintrack.repository.TransactionRepository;
import com.devansh.fintrack.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DashboardService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public DashboardService(TransactionRepository transactionRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    public DashboardSummaryResponseDto getSummary(){

        User currentUser = getCurrentUser();

        BigDecimal totalIncome =
                transactionRepository.getTotalAmountByType(TransactionType.INCOME, currentUser.getId());

        BigDecimal totalExpense =
                transactionRepository.getTotalAmountByType(TransactionType.EXPENSE, currentUser.getId());

        BigDecimal balance = totalIncome.subtract(totalExpense);

        DashboardSummaryResponseDto response = new DashboardSummaryResponseDto();

        response.setTotalIncome(totalIncome);
        response.setTotalExpense(totalExpense);
        response.setBalance(balance);

        return response;
    }

    public List<CategoryExpenseResponseDto> getCategoryWiseExpenses(){

        User currentUser = getCurrentUser();

        List<Object[]> results =
                transactionRepository.getCategoryWiseExpenses(currentUser.getId());

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

        User currentUser = getCurrentUser();

        List<Object[]> results =
                transactionRepository.getMonthlySummary(currentUser.getId());

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
    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Current user not found"
                        ));
    }
}
