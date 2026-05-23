package com.ecommerce.cartservice.controller;

import com.ecommerce.cartservice.dto.CartRequestDTO;
import com.ecommerce.cartservice.dto.CartResponseDTO;
import com.ecommerce.cartservice.dto.CartItemRequestDTO;
import com.ecommerce.cartservice.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("cart")
public class CartController {

    private final CartService cartService;
    private final String REMOVE_MSG = "Product removed from cart successfully.";
    private final String CLEAR_MSG = "Cart cleared successfully.";

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<CartResponseDTO> getCart(@PathVariable Long userId) {
        var response = cartService.getCart(userId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping
    public ResponseEntity<CartResponseDTO> createCart(@Valid @RequestBody CartRequestDTO dto) {
        var response = cartService.createCart(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/user/{userId}/items")
    public ResponseEntity<CartResponseDTO> addProductToCart(
            @PathVariable Long userId,
            @Valid @RequestBody CartItemRequestDTO dto
    ) {
        var response = cartService.addProductToCart(userId, dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/user/{userId}/items")
    public ResponseEntity<CartResponseDTO> updateProductQuantity(
            @PathVariable Long userId,
            @Valid @RequestBody CartItemRequestDTO dto
    ) {
        var response = cartService.updateProductQuantity(userId, dto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @DeleteMapping("/user/{userId}/product/{productId}")
    public ResponseEntity<String> removeProductFromCart(@PathVariable Long userId, @PathVariable Long productId) {
        cartService.removeProductFromCart(userId, productId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(REMOVE_MSG);
    }

    @DeleteMapping("/user/{userId}/clear")
    public ResponseEntity<String> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CLEAR_MSG);
    }
}
