package com.ecommerce.cartservice.mapper;

import com.ecommerce.cartservice.dto.CartItemRequestDTO;
import com.ecommerce.cartservice.dto.CartItemResponseDTO;
import com.ecommerce.cartservice.dto.CartRequestDTO;
import com.ecommerce.cartservice.dto.CartResponseDTO;
import com.ecommerce.cartservice.entity.CartEntity;
import com.ecommerce.cartservice.entity.CartItemEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CartMapper {

    public CartEntity toEntity(CartRequestDTO dto) {
        CartEntity cart = new CartEntity();
        cart.setUserId(dto.getUserId());

        List<CartItemEntity> items = dto.getItems()
                .stream()
                .map(itemDto -> toItemEntity(itemDto, cart))
                .toList();

        cart.getItems().addAll(items);
        return cart;
    }

    public CartItemEntity toItemEntity(CartItemRequestDTO dto, CartEntity cart) {
        CartItemEntity item = new CartItemEntity();
        item.setCart(cart);
        item.setProductId(dto.getProductId());
        item.setQuantity(dto.getQuantity());
        return item;
    }

    public CartResponseDTO toResponse(CartEntity cart) {
        return CartResponseDTO.builder()
                .cartId(cart.getCartId())
                .userId(cart.getUserId())
                .items(toItemResponseList(cart.getItems()))
                .createdAt(cart.getCreatedAt())
                .updatedAt(cart.getUpdatedAt())
                .build();
    }

    public CartItemResponseDTO toItemResponse(CartItemEntity item) {
        return CartItemResponseDTO.builder()
                .cartItemId(item.getCartItemId())
                .productId(item.getProductId())
                .quantity(item.getQuantity())
                .build();
    }

    public List<CartItemResponseDTO> toItemResponseList(List<CartItemEntity> items) {
        return items.stream()
                .map(this::toItemResponse)
                .toList();
    }

    public void updateItemEntity(CartItemEntity item, CartItemRequestDTO dto) {
        item.setProductId(dto.getProductId());
        item.setQuantity(dto.getQuantity());
    }
}
