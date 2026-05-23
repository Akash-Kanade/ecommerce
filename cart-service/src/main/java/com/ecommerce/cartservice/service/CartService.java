package com.ecommerce.cartservice.service;

import com.ecommerce.cartservice.dto.CartRequestDTO;
import com.ecommerce.cartservice.dto.CartResponseDTO;
import com.ecommerce.cartservice.dto.CartItemRequestDTO;
import com.ecommerce.cartservice.exception.ProductException;

public interface CartService {

    CartResponseDTO getCart(Long userId);

    CartResponseDTO createCart(CartRequestDTO dto) throws ProductException;

    CartResponseDTO addProductToCart(Long userId, CartItemRequestDTO dto);

    CartResponseDTO updateProductQuantity(Long userId, CartItemRequestDTO dto);

    void removeProductFromCart(Long userId, Long productId);

    void clearCart(Long userId);
}
