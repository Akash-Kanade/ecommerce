package com.ecommerce.cartservice.utility;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class Product {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private int stockQuantity;
    private String sku;
    private Boolean active;
}
