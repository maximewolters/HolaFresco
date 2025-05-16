// src/main/java/com/holafresco/catalog/controllers/IngredientController.java
package com.holafresco.catalog.controllers;

import com.holafresco.catalog.domain.Ingredient;
import com.holafresco.catalog.exceptions.IngredientNotFoundException;
import com.holafresco.catalog.repository.IngredientRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ingredients")
public class IngredientController {

    private final IngredientRepository repository;

    public IngredientController(IngredientRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Ingredient> all() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Ingredient one(@PathVariable Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new IngredientNotFoundException(id));
    }

    @PostMapping
    public Ingredient create(@RequestBody Ingredient ing) {
        return repository.save(ing);
    }

    @PutMapping("/{id}")
    public Ingredient update(@RequestBody Ingredient updated,
                             @PathVariable Long id) {
        return repository.findById(id)
            .map(i -> {
                i.setName(updated.getName());
                i.setCaloriesPerUnit(updated.getCaloriesPerUnit());
                i.setStockLevel(updated.getStockLevel());
                i.setUnit(updated.getUnit());
                return repository.save(i);
            })
            .orElseThrow(() -> new IngredientNotFoundException(id));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
