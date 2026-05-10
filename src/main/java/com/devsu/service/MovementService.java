package com.devsu.service;

import com.devsu.model.Movement;
import com.devsu.repository.MovementRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovementService {

    private final MovementRepository movementRepository;

    public MovementService(MovementRepository movementRepository) {
        this.movementRepository = movementRepository;
    }

    public List<Movement> getAll() {
        return movementRepository.findAll();
    }

    public Optional<Movement> getById(Long id) {
        return movementRepository.findById(id);
    }

    public Movement create(Movement movement) {
        return movementRepository.save(movement);
    }

    public Optional<Movement> update(Long id, Movement updatedMovement) {
        return movementRepository.findById(id).map(existing -> {
            existing.setDate(updatedMovement.getDate());
            existing.setMovementType(updatedMovement.getMovementType());
            existing.setAmount(updatedMovement.getAmount());
            existing.setBalance(updatedMovement.getBalance());
            return movementRepository.save(existing);
        });
    }

    public boolean delete(Long id) {
        if (movementRepository.existsById(id)) {
            movementRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
