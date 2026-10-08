package com.devansh.fintrack.service;

import com.devansh.fintrack.dto.request.CreateTransactionRequestDto;


import com.devansh.fintrack.dto.request.UpdateTransactionRequestDto;

import com.devansh.fintrack.dto.response.TransactionResponseDto;
import com.devansh.fintrack.entity.Category;
import com.devansh.fintrack.entity.Transaction;
import com.devansh.fintrack.entity.User;
import com.devansh.fintrack.exception.ResourceNotFoundException;
import com.devansh.fintrack.filter.TransactionFilter;
import com.devansh.fintrack.filter.TransactionSpecification;
import com.devansh.fintrack.repository.CategoryRepository;
import com.devansh.fintrack.repository.TransactionRepository;
import com.devansh.fintrack.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    private final UserRepository userRepository;

    private final CategoryRepository categoryRepository;

    public TransactionService(
            TransactionRepository transactionRepository
            , UserRepository userRepository
            , CategoryRepository categoryRepository){

        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public TransactionResponseDto createTransaction(CreateTransactionRequestDto request) {

        User user = getCurrentUser();

        Category category = categoryRepository.findByIdAndUserId(request.getCategoryId(),user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category with id " + request.getCategoryId() + " not found"));

        Transaction transaction = mapToEntity(request, user, category);

        Transaction savedTransaction = transactionRepository.save(transaction);

        return mapToDto(savedTransaction);
    }


    public TransactionResponseDto getTransaction(Long id ){
        User currentUser = getCurrentUser();

        Transaction transaction =
                transactionRepository.findByIdAndUserId(
                                id,
                                currentUser.getId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Transaction with id " + id + " not found"));

        return mapToDto(transaction);
    }


    public Page<TransactionResponseDto> getAllTransactions(
            int page,
            int size,
            String sortBy,
            String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        User currentUser = getCurrentUser();

        Page<Transaction> transactions =
                transactionRepository.findAllByUserId(
                        currentUser.getId(),
                        pageable
                );

        return transactions.map(this::mapToDto);
    }

    @Transactional
    public TransactionResponseDto updateTransaction(
            Long id, UpdateTransactionRequestDto updateTransaction) {

        User currentUser = getCurrentUser();

        Transaction existingTransaction =
                transactionRepository.findByIdAndUserId(
                                id,
                                currentUser.getId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Transaction with id " + id + " not found"));

        Category category = categoryRepository.findByIdAndUserId(updateTransaction.getCategoryId(), currentUser.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category with id " + updateTransaction.getCategoryId() + " not found"));

        existingTransaction.setTitle(updateTransaction.getTitle());
        existingTransaction.setAmount(updateTransaction.getAmount());
        existingTransaction.setDescription(updateTransaction.getDescription());

        existingTransaction.setType(updateTransaction.getType());
        existingTransaction.setCategory(category);

        Transaction savedTransaction = transactionRepository.save(existingTransaction);

        return mapToDto(savedTransaction);
    }
    @Transactional
    public void deleteTransaction(Long id){
        User currentUser = getCurrentUser();

        Transaction transaction =
                transactionRepository.findByIdAndUserId(
                                id,
                                currentUser.getId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Transaction with id " + id + " not found"));

        transactionRepository.delete(transaction);

    }

    public List<TransactionResponseDto> filterTransaction(TransactionFilter filter) {

        User currentUser = getCurrentUser();

        Specification<Transaction> specification =
                TransactionSpecification.filter(filter)
                        .and((root, query, criteriaBuilder) ->
                                criteriaBuilder.equal(
                                        root.get("user").get("id"),
                                        currentUser.getId()
                                ));

        List<Transaction> transactions =
                transactionRepository.findAll(specification);

        return transactions.stream()
                .map(this::mapToDto)
                .toList();
    }



    private Transaction mapToEntity(CreateTransactionRequestDto request,
                                   User user , Category category){

        Transaction transaction = new Transaction();

        transaction.setTitle(request.getTitle());
        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setType(request.getType());

        transaction.setUser(user);
        transaction.setCategory(category);

        return transaction;
    }

    private TransactionResponseDto mapToDto(Transaction transaction){

        TransactionResponseDto response = new TransactionResponseDto();

        response.setId(transaction.getId());
        response.setTitle(transaction.getTitle());
        response.setAmount(transaction.getAmount());
        response.setDescription(transaction.getDescription());
        response.setTransactionDate(transaction.getTransactionDate());
        response.setType(transaction.getType());
        response.setUserId(transaction.getUser().getId());
        response.setCategoryId(transaction.getCategory().getId());
        response.setCreatedAt(transaction.getCreatedAt());
        response.setUpdatedAt(transaction.getUpdatedAt());

        return response;
    }

    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Current user not found"));
    }
}
