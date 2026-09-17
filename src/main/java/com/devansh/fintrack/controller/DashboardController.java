package com.devansh.fintrack.controller;

import com.devansh.fintrack.dto.response.CategoryExpenseResponseDto;
import com.devansh.fintrack.dto.response.DashboardSummaryResponseDto;
import com.devansh.fintrack.dto.response.MonthlySummaryResponseDto;
import com.devansh.fintrack.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/summary")
    public ResponseEntity<DashboardSummaryResponseDto> getSummary(){

        DashboardSummaryResponseDto summary =
                dashboardService.getSummary();

        return ResponseEntity.ok(summary);
    }

    @GetMapping("/category-expenses")
    public ResponseEntity<List<CategoryExpenseResponseDto>> getAllCategoryWiseExpenses(){

        List<CategoryExpenseResponseDto> expenses = dashboardService.getCategoryWiseExpenses();

        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/monthly-summary")
    public ResponseEntity<List<MonthlySummaryResponseDto>> getMonthlySummary() {

        List<MonthlySummaryResponseDto> summary =
                dashboardService.getMonthlySummary();

        return ResponseEntity.ok(summary);
    }
}
