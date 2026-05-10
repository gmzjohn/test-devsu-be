package com.devsu.repository;

import com.devsu.model.Account;
import com.devsu.model.Movement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovementRepository extends JpaRepository<Movement, Long> {

    Optional<Movement> findTopByAccountOrderByIdDesc(Account account);
}
