// src/main/java/com/holafresco/catalog/exceptions/IngredientNotFoundException.java
package com.holafresco.catalog.exceptions;

public class IngredientNotFoundException extends RuntimeException {
    public IngredientNotFoundException(Long id) {
        super("Could not find ingredient " + id);
    }
}
