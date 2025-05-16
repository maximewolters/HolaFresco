// src/main/java/com/holafresco/catalog/repository/IngredientRepository.java
package com.holafresco.catalog.repository;

import com.holafresco.catalog.domain.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> { }

