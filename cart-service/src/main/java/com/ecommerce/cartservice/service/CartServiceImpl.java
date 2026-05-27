package com.ecommerce.cartservice.service;

import com.ecommerce.cartservice.dto.CartRequestDTO;
import com.ecommerce.cartservice.dto.CartResponseDTO;
import com.ecommerce.cartservice.dto.CartItemRequestDTO;
import com.ecommerce.cartservice.entity.CartEntity;
import com.ecommerce.cartservice.entity.CartItemEntity;
import com.ecommerce.cartservice.exception.ProductException;
import com.ecommerce.cartservice.mapper.CartMapper;
import com.ecommerce.cartservice.repository.CartRepository;
import com.ecommerce.cartservice.utility.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestClient;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;


@Service
public class CartServiceImpl implements CartService{
    private final  CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final RestClient restClient;
    private final String getProduct ="/get/";
    @Value("${product.service.base-url}")
    private String productBaseUrl;
    CartServiceImpl(CartRepository cartRepository, CartMapper cartMapper, RestClient restClient) {
        this.cartMapper = cartMapper;
        this.cartRepository = cartRepository;
        this.restClient = restClient;
    }

    @Override
    @Transactional(readOnly = true)
    public CartResponseDTO getCart(Long userId) {
        CartEntity cart = getCartByUserId(userId);
        return cartMapper.toResponse(cart);
    }

    @Override
    @Transactional
    public CartResponseDTO createCart(CartRequestDTO dto) {
        if(cartRepository.existsByUserId(dto.getUserId()))
            throw new RuntimeException("Cart with userId "+dto.getUserId()+" already exists");

        CartEntity cart = cartMapper.toEntity(dto);
        mergeDuplicateCartItems(cart);

        validateCartItems(cart);
        var savedCart = cartRepository.save(cart);
        return cartMapper.toResponse(savedCart);
    }

    @Override
    @Transactional
    public CartResponseDTO addProductToCart(Long userId, CartItemRequestDTO dto) {
        CartEntity cart = getCartByUserId(userId);
        CartItemEntity existingItem = findCartItem(cart, dto.getProductId()).orElse(null);

        if (existingItem == null) {
            CartItemEntity item = cartMapper.toItemEntity(dto, cart);
            validateCartItem(item);
            cart.getItems().add(item);
        } else {
            existingItem.setQuantity(existingItem.getQuantity() + dto.getQuantity());
            validateCartItem(existingItem);
        }

        CartEntity savedCart = cartRepository.save(cart);
        return cartMapper.toResponse(savedCart);
    }

    @Override
    @Transactional
    public CartResponseDTO updateProductQuantity(Long userId, CartItemRequestDTO dto) {
        CartEntity cart = getCartByUserId(userId);
        CartItemEntity item = findCartItem(cart, dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product with id " + dto.getProductId() + " is not in cart"));

        item.setQuantity(dto.getQuantity());
        validateCartItem(item);

        CartEntity savedCart = cartRepository.save(cart);
        return cartMapper.toResponse(savedCart);
    }

    @Override
    @Transactional
    public void removeProductFromCart(Long userId, Long productId) {
        CartEntity cart = getCartByUserId(userId);
        CartItemEntity item = findCartItem(cart, productId)
                .orElseThrow(() -> new RuntimeException("Product with id " + productId + " is not in cart"));

        cart.getItems().remove(item);
        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void clearCart(Long userId) {
        CartEntity cart = getCartByUserId(userId);
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    private CartEntity getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart with userId " + userId + " does not exist"));
    }

    private void validateCartItems(CartEntity cart) {
        for(CartItemEntity cartItemEntity : cart.getItems())
        {
            validateCartItem(cartItemEntity);
        }
    }

    private void validateCartItem(CartItemEntity cartItemEntity) throws ProductException {
        Product product = getProduct(cartItemEntity.getProductId());
        if(product == null)
            throw new ProductException("Product not found");
        if(!Boolean.TRUE.equals(product.getActive()))
            throw new ProductException("Invalid Product !");
        if(product.getStockQuantity() < cartItemEntity.getQuantity()
                || cartItemEntity.getQuantity() < 1)
            throw new ProductException("Invalid quantity");
    }

    private Product getProduct(Long productId) throws ProductException {
        try {
            return restClient.get()
                    .uri(this.productBaseUrl + this.getProduct + productId)
                    .retrieve()
                    .body(Product.class);
        } catch (RestClientResponseException e) {
            throw new ProductException("Product with id " + productId + " not found");
        }
    }

    private Optional<CartItemEntity> findCartItem(CartEntity cart, Long productId) {
        return cart.getItems()
                .stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();
    }

    private void mergeDuplicateCartItems(CartEntity cart) {
        Map<Long, CartItemEntity> mergedItems = new LinkedHashMap<>();
        for (CartItemEntity item : cart.getItems()) {
            CartItemEntity existingItem = mergedItems.get(item.getProductId());
            if (existingItem == null) {
                mergedItems.put(item.getProductId(), item);
            } else {
                existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
            }
        }

        cart.getItems().clear();
        cart.getItems().addAll(mergedItems.values());
    }
}
