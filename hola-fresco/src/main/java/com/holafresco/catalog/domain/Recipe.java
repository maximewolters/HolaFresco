// src/main/java/com/holafresco/catalog/domain/Recipe.java
package com.holafresco.catalog.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private BigDecimal price;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeIngredient> ingredients = new ArrayList<>();

    protected Recipe() { }

    public Recipe(String name, String description, BigDecimal price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public List<RecipeIngredient> getIngredients() { return ingredients; }

    /** helper to add one ingredient+qty */
    public void addIngredient(Ingredient ing, BigDecimal qty) {
        RecipeIngredient ri = new RecipeIngredient(this, ing, qty);
        ingredients.add(ri);
    }

    /** helper to remove */
    public void removeIngredient(RecipeIngredient ri) {
        ingredients.remove(ri);
        ri.setRecipe(null);
        ri.setIngredient(null);
    }
}
