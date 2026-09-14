package com.devansh.fintrack.repository;

import com.devansh.fintrack.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String emailId);

    boolean existsByEmailAndIdNot(String email, Long id);

}
