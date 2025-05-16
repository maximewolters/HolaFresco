package com.holafresco.cart.repository;

import com.holafresco.cart.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.Instant;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByClientIdAndStatus(String clientId, Cart.Status status);

    /**
     * Sum up all quantities from carts that were ORDERED since :since.
     */
    @Query("""
      SELECT COALESCE(SUM(i.quantity),0)
      FROM Cart c
      JOIN c.items i
      WHERE c.clientId = :clientId
        AND c.status = 'ORDERED'
        AND c.createdAt >= :since
      """)
    int totalBoxesThisWeek(String clientId, Instant since);
}

