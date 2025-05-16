// src/main/java/com/holafresco/catalog/exceptions/IngredientNotFoundAdvice.java
package com.holafresco.catalog.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class IngredientNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(IngredientNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String ingredientNotFoundHandler(IngredientNotFoundException ex) {
        return ex.getMessage();
    }
}
