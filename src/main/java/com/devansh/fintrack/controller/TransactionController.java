package com.devansh.fintrack.controller;

import com.devansh.fintrack.dto.request.CreateTransactionRequestDto;
import com.devansh.fintrack.dto.request.UpdateTransactionRequestDto;
import com.devansh.fintrack.dto.response.TransactionResponseDto;
import com.devansh.fintrack.filter.TransactionFilter;
import com.devansh.fintrack.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<TransactionResponseDto> createTransaction(
            @Valid @RequestBody CreateTransactionRequestDto request) {

        TransactionResponseDto response = transactionService.createTransaction(request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDto> getTransaction(
            @PathVariable Long id) {

        TransactionResponseDto transaction = transactionService.getTransaction(id);

        return ResponseEntity.ok(transaction);
    }

    @GetMapping
    public ResponseEntity<Page<TransactionResponseDto>> getAllTransaction(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

       Page<TransactionResponseDto> transactions =
                transactionService.getAllTransactions(
                        page, size, sortBy, direction);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<TransactionResponseDto>> filterTransaction(
            @ModelAttribute TransactionFilter filter){

        List<TransactionResponseDto> transactions = transactionService.filterTransaction(filter);

        return ResponseEntity.ok(transactions);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponseDto> updateTransaction(
           @PathVariable Long id, @Valid @RequestBody UpdateTransactionRequestDto request){

        TransactionResponseDto transaction = transactionService.updateTransaction(id, request);

        return ResponseEntity.ok(transaction);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id){

        transactionService.deleteTransaction(id);

        return ResponseEntity.noContent().build();
    }
}
