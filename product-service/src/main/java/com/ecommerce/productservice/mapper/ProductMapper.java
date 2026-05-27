package com.ecommerce.productservice.mapper;

import com.ecommerce.productservice.dto.ProductRequestDTO;
import com.ecommerce.productservice.dto.ProductResponseDTO;
import com.ecommerce.productservice.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toEntity(ProductRequestDTO requestDTO)
    {
        Product product = new Product();
        product.setName(requestDTO.getName());
        product.setDescription(requestDTO.getDescription());
        product.setPrice(requestDTO.getPrice());
        product.setSku(requestDTO.getSku());
        product.setStockQuantity(requestDTO.getStockQuantity());
        product.setActive(requestDTO.getActive());
        product.setImageUrl(requestDTO.getImageUrl());
        return product;
    }

    public ProductResponseDTO toResponse(Product product)
    {
        return ProductResponseDTO.builder()
                .id(product.getProductId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .sku(product.getSku())
                .active(product.getActive())
                .imageUrl(product.getImageUrl()).build();
    }

    public void updateEntity(Product product, ProductRequestDTO dto) {
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStockQuantity(dto.getStockQuantity());
        product.setSku(dto.getSku());
        product.setActive(dto.getActive());
        product.setImageUrl(dto.getImageUrl());
    }
}
