package com.holafresco.cart.services;

import com.holafresco.cart.dto.CartDTO;
import com.holafresco.cart.exceptions.CartNotFoundException;
import com.holafresco.cart.exceptions.LimitExceededException;
import com.holafresco.cart.domain.Cart;
import com.holafresco.cart.domain.CartItem;
import com.holafresco.cart.domain.Reservation;
import com.holafresco.catalog.domain.Ingredient;
import com.holafresco.catalog.domain.Recipe;
import com.holafresco.cart.repository.CartItemRepository;
import com.holafresco.cart.repository.CartRepository;
import com.holafresco.cart.repository.ReservationRepository;
import com.holafresco.catalog.repository.IngredientRepository;
import com.holafresco.catalog.repository.RecipeRepository;
import com.holafresco.cart.mapper.CartMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class CartService {

    private static final int WEEKLY_LIMIT = 14;
    private static final long CART_EXPIRY_HOURS = 1;

    private final CartRepository cartRepo;
    private final CartItemRepository itemRepo;
    private final ReservationRepository resRepo;
    private final RecipeRepository recipeRepo;
    private final IngredientRepository ingredientRepo;
    private final CartMapper mapper;

    public CartService(CartRepository cartRepo,
                       CartItemRepository itemRepo,
                       ReservationRepository resRepo,
                       RecipeRepository recipeRepo,
                       IngredientRepository ingredientRepo,
                       CartMapper mapper) {
        this.cartRepo = cartRepo;
        this.itemRepo = itemRepo;
        this.resRepo = resRepo;
        this.recipeRepo = recipeRepo;
        this.ingredientRepo = ingredientRepo;
        this.mapper = mapper;
    }

    public CartDTO getOrCreateCart(String clientId) {
        Cart cartEntity = cartRepo.findByClientIdAndStatus(clientId, Cart.Status.OPEN)
            .map(this::refreshExpiry)
            .orElseGet(() -> createNewCartEntity(clientId));
        return mapper.toDto(cartEntity);
    }

    private Cart createNewCartEntity(String clientId) {
        Instant now = Instant.now();
        Cart cart = new Cart();
        cart.setClientId(clientId);
        cart.setCreatedAt(now);
        cart.setExpiresAt(now.plus(CART_EXPIRY_HOURS, ChronoUnit.HOURS));
        cart.setStatus(Cart.Status.OPEN);
        return cartRepo.save(cart);
    }

    private Cart refreshExpiry(Cart cart) {
        cart.setExpiresAt(Instant.now().plus(CART_EXPIRY_HOURS, ChronoUnit.HOURS));
        return cartRepo.save(cart);
    }

    public CartDTO getCart(String clientId) {
        Cart cart = cartRepo.findByClientIdAndStatus(clientId, Cart.Status.OPEN)
            .orElseThrow(() -> new CartNotFoundException(clientId));
        return mapper.toDto(cart);
    }

    @Transactional
    public CartDTO addItemToCart(String clientId,
                                 Long recipeId,
                                 int quantity) {
        // 1) Ensure cart exists
        Cart cart = cartRepo.findByClientIdAndStatus(clientId, Cart.Status.OPEN)
            .orElseGet(() -> createNewCartEntity(clientId));

        // 2) Enforce 14-box per week limit
        int used = cartRepo.totalBoxesThisWeek(
                clientId,
                Instant.now().minus(7, ChronoUnit.DAYS));
        if (used + quantity > WEEKLY_LIMIT) {
            throw new LimitExceededException(
                "Weekly limit of " + WEEKLY_LIMIT + " exceeded");
        }

        // 3) Load recipe + compute needed ingredients
        Recipe recipe = recipeRepo.findById(recipeId)
            .orElseThrow(() -> new NoSuchElementException(
                "Recipe " + recipeId));
        Map<Ingredient, java.math.BigDecimal> needed = new HashMap<>();
        recipe.getIngredients().forEach(ri ->
            needed.put(
                ri.getIngredient(),
                ri.getQuantity()
                  .multiply(java.math.BigDecimal.valueOf(quantity))
            )
        );

        // 4) Check & reserve stock
        needed.forEach((ing, amount) -> {
            java.math.BigDecimal reserved =
                resRepo.totalReservedForIngredient(ing.getId());
            java.math.BigDecimal available =
                ing.getStockLevel().subtract(reserved);
            if (available.compareTo(amount) < 0) {
                throw new IllegalStateException(
                    "Out of stock for " + ing.getName());
            }
        });

        // 5) Build & save the CartItem
        Instant now = Instant.now();
        CartItem toSave = new CartItem(cart, recipeId, quantity, now);
        CartItem savedItem = itemRepo.save(toSave);

        // 6) Persist reservations
        needed.forEach((ing, amount) -> {
            Reservation r = new Reservation(
                savedItem,
                ing.getId(),
                amount
            );
            resRepo.save(r);
            savedItem.getReservations().add(r);
        });

        // 7) Refresh cart expiry
        cart.setExpiresAt(now.plus(CART_EXPIRY_HOURS, ChronoUnit.HOURS));
        cartRepo.save(cart);

        // 8) Return DTO
        return mapper.toDto(cart);
    }
}


