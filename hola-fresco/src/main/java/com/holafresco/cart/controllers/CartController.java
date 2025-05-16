package com.holafresco.cart.controllers;

import com.holafresco.cart.dto.CartDTO;
import com.holafresco.cart.dto.CartItemDTO;
import com.holafresco.cart.services.CartService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.UUID;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private static final String COOKIE_NAME = "clientId";
    private static final int COOKIE_MAX_AGE = (int) Duration.ofDays(30).getSeconds();

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    /**
     * Create or fetch the current cart for this anonymous user.
     * If no clientId cookie is present, we generate one and set it.
     */
    @PostMapping
    public ResponseEntity<CartDTO> createOrGetCart(
        @CookieValue(value = COOKIE_NAME, required = false) String clientId,
        HttpServletResponse response
    ) {
        // generate clientId if missing
        if (clientId == null || clientId.isBlank()) {
            clientId = UUID.randomUUID().toString();
            Cookie cookie = new Cookie(COOKIE_NAME, clientId);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setMaxAge(COOKIE_MAX_AGE);
            response.addCookie(cookie);
        }

        CartDTO cart = cartService.getOrCreateCart(clientId);
        return ResponseEntity.ok(cart);
    }

    /**
     * Add a recipe box to the cart (with quantity = 1 by default).
     * Expects a JSON body { "recipeId": 123, "quantity": 2 }.
     */
    @PostMapping("/items")
    public ResponseEntity<CartDTO> addItem(
        @RequestBody CartItemDTO item,
        @CookieValue(value = COOKIE_NAME, required = false) String clientId,
        HttpServletResponse response
    ) {
        // ensure we have a clientId
        if (clientId == null || clientId.isBlank()) {
            clientId = UUID.randomUUID().toString();
            Cookie cookie = new Cookie(COOKIE_NAME, clientId);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setMaxAge(COOKIE_MAX_AGE);
            response.addCookie(cookie);
        }

        CartDTO updated = cartService.addItemToCart(clientId, item.getRecipeId(), item.getQuantity());
        return ResponseEntity.ok(updated);
    }

    /**
     * View the current cart for this anonymous user.
     */
    @GetMapping
    public ResponseEntity<CartDTO> getCart(
        @CookieValue(value = COOKIE_NAME, required = false) String clientId
    ) {
        if (clientId == null || clientId.isBlank()) {
            return ResponseEntity.notFound().build();
        }
        CartDTO cart = cartService.getCart(clientId);
        return ResponseEntity.ok(cart);
    }
}
