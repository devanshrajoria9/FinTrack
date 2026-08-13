package com.devansh.fintrack.controller;

import com.devansh.fintrack.dto.request.CreateCategoryRequestDto;
import com.devansh.fintrack.dto.request.CreateTransactionRequestDto;
import com.devansh.fintrack.dto.request.UpdateTransactionRequestDto;
import com.devansh.fintrack.dto.response.TransactionResponseDto;
import com.devansh.fintrack.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService){
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<TransactionResponseDto> createTransaction(
            @Valid @RequestBody CreateTransactionRequestDto request){

        TransactionResponseDto response = transactionService.createTransaction(request);

        return ResponseEntity.ok(response);
    }
   @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDto> getTransaction(
            @PathVariable Long id){

        TransactionResponseDto transaction = transactionService.getTransaction(id);

        return ResponseEntity.ok(transaction);
    }
    @GetMapping
    public ResponseEntity<List<TransactionResponseDto>> getAllTransaction(){

       List<TransactionResponseDto> transactions= transactionService.getAllTransactions();

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
