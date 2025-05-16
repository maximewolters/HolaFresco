package com.holafresco.cart.mapper;

import com.holafresco.cart.domain.Cart;
import com.holafresco.cart.domain.CartItem;
import com.holafresco.cart.dto.CartDTO;
import com.holafresco.cart.dto.CartItemDTO;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CartMapper {

    public CartDTO toDto(Cart cart) {
        CartDTO dto = new CartDTO();
        dto.setId(cart.getId());
        dto.setClientId(cart.getClientId());
        dto.setCreatedAt(cart.getCreatedAt());
        dto.setExpiresAt(cart.getExpiresAt());
        dto.setItems(
            cart.getItems()
                .stream()
                .map(this::toItemDto)
                .collect(Collectors.toList())
        );
        return dto;
    }

    public CartItemDTO toItemDto(CartItem item) {
        return new CartItemDTO(
            item.getRecipeId(),
            item.getQuantity()
        );
    }

    /**
     * If you need to create a new Cart entity from scratch:
     */
    public Cart toEntity(String clientId, CartDTO dto) {
        Cart cart = new Cart();
        cart.setClientId(clientId);
        cart.setCreatedAt(dto.getCreatedAt());
        cart.setExpiresAt(dto.getExpiresAt());
        dto.getItems().forEach(itemDto -> {
            CartItem item = new CartItem(
                cart,
                itemDto.getRecipeId(),
                itemDto.getQuantity(),
                dto.getCreatedAt()
            );
            cart.getItems().add(item);
        });
        return cart;
    }
}

