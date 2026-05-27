package com.ecommerce.productservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductRequestDTO {

    private String name;
    private String description;
    private BigDecimal price;
    private int stockQuantity;
    private String sku;
    private Boolean active;
    private String imageUrl;
}
