package com.devsu.controller;

import com.devsu.model.Movement;
import com.devsu.service.MovementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movements")
public class MovementController {

    private final MovementService movementService;

    public MovementController(MovementService movementService) {
        this.movementService = movementService;
    }

    @GetMapping
    public ResponseEntity<List<Movement>> getAll() {
        return ResponseEntity.ok(movementService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movement> getById(@PathVariable Long id) {
        return movementService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Movement> create(@RequestBody Movement movement) {
        return ResponseEntity.status(HttpStatus.CREATED).body(movementService.create(movement));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Movement> update(@PathVariable Long id, @RequestBody Movement movement) {
        return movementService.update(id, movement)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (movementService.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
