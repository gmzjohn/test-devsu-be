package com.devsu.repository;

import com.devsu.model.Account;
import com.devsu.model.Movement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovementRepository extends JpaRepository<Movement, Long> {

    Optional<Movement> findTopByAccountOrderByIdDesc(Account account);

    List<Movement> findByAccountAndDateBetween(Account account, String startDate, String endDate);
}
