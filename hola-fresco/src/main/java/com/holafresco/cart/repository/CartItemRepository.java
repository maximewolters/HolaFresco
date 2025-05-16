package com.holafresco.cart.repository;

import com.holafresco.cart.domain.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    // you can add findByCartId(...) here if needed
}
