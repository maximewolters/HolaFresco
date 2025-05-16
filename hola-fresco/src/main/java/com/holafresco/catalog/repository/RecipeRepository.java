// src/main/java/com/holafresco/catalog/repository/RecipeRepository.java
package com.holafresco.catalog.repository;

import com.holafresco.catalog.domain.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Long> { }
