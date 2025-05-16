package com.holafresco.cart.dto;

public class CartItemDTO {
    private Long recipeId;
    private int quantity;

    public CartItemDTO() {}

    // convenience ctor
    public CartItemDTO(Long recipeId, int quantity) {
        this.recipeId = recipeId;
        this.quantity = quantity;
    }

    public Long getRecipeId() { return recipeId; }
    public void setRecipeId(Long recipeId) { this.recipeId = recipeId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
