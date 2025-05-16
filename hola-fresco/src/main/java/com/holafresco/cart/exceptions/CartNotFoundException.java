package com.holafresco.cart.exceptions;

public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException(String clientId) {
        super("No open cart found for clientId: " + clientId);
    }
}
