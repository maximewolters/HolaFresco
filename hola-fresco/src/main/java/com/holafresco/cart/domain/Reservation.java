package com.holafresco.cart.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // which CartItem holds this reservation
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "item_id")
    private CartItem item;

    // refer to Ingredient by its ID
    @Column(name = "ingredient_id", nullable = false)
    private Long ingredientId;

    // how much is reserved
    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    public Reservation() {}

    public Reservation(CartItem item, Long ingredientId, BigDecimal amount) {
        this.item = item;
        this.ingredientId = ingredientId;
        this.amount = amount;
    }

    // --- getters & setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public CartItem getItem() { return item; }
    public void setItem(CartItem item) { this.item = item; }

    public Long getIngredientId() { return ingredientId; }
    public void setIngredientId(Long ingredientId) { this.ingredientId = ingredientId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}

