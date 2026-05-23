package com.ecommerce.cartservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CartItemResponseDTO {

    private Long cartItemId;
    private Long productId;
    private Integer quantity;
}
