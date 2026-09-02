package com.devansh.fintrack.filter;

import com.devansh.fintrack.entity.Transaction;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class TransactionSpecification {

    public static Specification<Transaction> filter(TransactionFilter filter) {

        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (filter.getType() != null) {

                predicates.add(
                        criteriaBuilder.equal(
                                root.get("type"),
                                filter.getType()
                        )
                );
            }
            if(filter.getCategoryId() != null){
                predicates.add(
                        criteriaBuilder.equal(
                                root.get("category").get("id"),
                                filter.getCategoryId()
                        )
                );
            }
            if(filter.getMinAmount() != null){

                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(
                                root.get("amount"),
                                filter.getMinAmount()
                        )
                );
            }
            if(filter.getMaxAmount() != null){

                predicates.add(
                        criteriaBuilder.lessThanOrEqualTo(
                                root.get("amount"),
                                filter.getMaxAmount()
                        )
                );
            }
            if (filter.getStartDate() != null) {

                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(
                                root.get("transactionDate"),
                                filter.getStartDate()
                        )
                );
            }
            if (filter.getEndDate() != null) {

                predicates.add(
                        criteriaBuilder.lessThanOrEqualTo(
                                root.get("transactionDate"),
                                filter.getEndDate()
                        )
                );
            }

            if (filter.getSearch() != null && !filter.getSearch().isBlank()){

                String search = "%" + filter.getSearch().toLowerCase() + "%";

                Predicate titlePredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("title")),
                        search
                );

                Predicate descriptionPredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("description")),
                        search
                );

                predicates.add(
                        criteriaBuilder.or(
                                titlePredicate,
                                descriptionPredicate
                        )
                );
            }

            return criteriaBuilder.and(
                    predicates.toArray(new Predicate[0])
            );
        };
    }
}
