// src/main/java/com/holafresco/catalog/controllers/RecipeController.java
package com.holafresco.catalog.controllers;

import com.holafresco.catalog.domain.Recipe;
import com.holafresco.catalog.exceptions.RecipeNotFoundException;
import com.holafresco.catalog.repository.RecipeRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeRepository repository;

    public RecipeController(RecipeRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Recipe> all() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Recipe one(@PathVariable Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new RecipeNotFoundException(id));
    }

    @PostMapping
    public Recipe create(@RequestBody Recipe newRecipe) {
        return repository.save(newRecipe);
    }

    @PutMapping("/{id}")
    public Recipe update(@RequestBody Recipe updated,
                         @PathVariable Long id) {
        return repository.findById(id)
            .map(r -> {
                r.setName(updated.getName());
                r.setDescription(updated.getDescription());
                r.setPrice(updated.getPrice());
                return repository.save(r);
            })
            .orElseThrow(() -> new RecipeNotFoundException(id));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
