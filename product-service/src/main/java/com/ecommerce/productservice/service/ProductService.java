package com.ecommerce.productservice.service;

import com.ecommerce.productservice.dto.ProductRequestDTO;
import com.ecommerce.productservice.dto.ProductResponseDTO;

import java.util.List;

public interface ProductService {
    public List<ProductResponseDTO> getAllProducts();
    public ProductResponseDTO getProduct(Long id);
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO dto);
    public ProductResponseDTO addProduct(ProductRequestDTO dto);
    public boolean deleteProduct(Long id);
}
