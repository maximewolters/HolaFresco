// src/main/java/com/holafresco/catalog/exceptions/RecipeNotFoundException.java
package com.holafresco.catalog.exceptions;

public class RecipeNotFoundException extends RuntimeException {
    public RecipeNotFoundException(Long id) {
        super("Could not find recipe " + id);
    }
}
