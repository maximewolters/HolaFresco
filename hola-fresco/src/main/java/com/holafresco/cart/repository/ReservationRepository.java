package com.holafresco.cart.repository;

import com.holafresco.cart.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.math.BigDecimal;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    /**
     * Sum all active reservations for a given ingredient.
     */
    @Query("""
      SELECT COALESCE(SUM(r.amount),0)
      FROM Reservation r
      JOIN r.item it
      JOIN it.cart c
      WHERE r.ingredientId = :ingredientId
        AND c.status = 'OPEN'
      """)
    BigDecimal totalReservedForIngredient(Long ingredientId);

    /**
     * Remove all reservations belonging to a cart (e.g. on expiry or cancellation).
     */
    void deleteByItem_Cart_Id(Long cartId);
}

