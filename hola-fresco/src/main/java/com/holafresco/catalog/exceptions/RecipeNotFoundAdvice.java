// src/main/java/com/holafresco/catalog/exceptions/RecipeNotFoundAdvice.java
package com.holafresco.catalog.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class RecipeNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(RecipeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String recipeNotFoundHandler(RecipeNotFoundException ex) {
        return ex.getMessage();
    }
}
