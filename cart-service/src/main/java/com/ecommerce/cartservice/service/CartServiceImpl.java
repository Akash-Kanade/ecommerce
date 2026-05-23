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
import org.springframework.web.client.RestClient;


@Service
public class CartServiceImpl implements CartService{
    private final  CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final RestClient restClient;
    private final String getProduct ="/get/";
    @Value("${PRODUCT_SERVICE_BASE_URL}")
    private String productBaseUrl;
    CartServiceImpl(CartRepository cartRepository, CartMapper cartMapper, RestClient restClient) {
        this.cartMapper = cartMapper;
        this.cartRepository = cartRepository;
        this.restClient = restClient;
    }

    @Override
    public CartResponseDTO getCart(Long userId) {
        return null;
    }

    @Override
    public CartResponseDTO createCart(CartRequestDTO dto) throws ProductException {
        if(cartRepository.existsByUserId(dto.getUserId()))
            throw new RuntimeException("Cart with userId "+dto.getUserId()+" already exists");

        CartEntity cart = cartMapper.toEntity(dto);
        boolean isDuplicate = cart.getItems().stream().map(CartItemEntity::getProductId).distinct().count() != cart.getItems().size();
        if(isDuplicate)
        {
            // Logic for merging duplicate product entries together.
        }

        validateCartItems(cart);
        var savedCart = cartRepository.save(cart);
        return cartMapper.toResponse(savedCart);
    }

    @Override
    public CartResponseDTO addProductToCart(Long userId, CartItemRequestDTO dto) {
        return null;
    }

    @Override
    public CartResponseDTO updateProductQuantity(Long userId, CartItemRequestDTO dto) {
        return null;
    }

    @Override
    public void removeProductFromCart(Long userId, Long productId) {

    }

    @Override
    public void clearCart(Long userId) {

    }

    private void validateCartItems(CartEntity cart) throws ProductException {
        for(CartItemEntity cartItemEntity : cart.getItems())
        {
            Product product = restClient.get()
                    .uri(this.productBaseUrl + this.getProduct + cartItemEntity.getProductId())
                    .retrieve()
                    .body(Product.class);
                if(product == null)
                    throw new ProductException("Product not found");
                if(!Boolean.TRUE.equals(product.getActive()))
                    throw new ProductException("Invalid Product !");
                if(product.getStockQuantity() < cartItemEntity.getQuantity()
                        || cartItemEntity.getQuantity() < 1)
                    throw new ProductException("Invalid quantity");
        }
    }
}
