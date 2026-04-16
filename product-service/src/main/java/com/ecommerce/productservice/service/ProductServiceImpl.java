package com.ecommerce.productservice.service;

import com.ecommerce.productservice.dto.ProductRequestDTO;
import com.ecommerce.productservice.dto.ProductResponseDTO;
import com.ecommerce.productservice.entity.Product;
import com.ecommerce.productservice.mapper.ProductMapper;
import com.ecommerce.productservice.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService{
    private final ProductRepository repository;
    private final ProductMapper mapper;

    ProductServiceImpl(ProductRepository repository, ProductMapper mapper)
    {
       this.repository = repository;
       this.mapper = mapper;
    }

    @Override
    public List<ProductResponseDTO> getAllProducts() {
        List<Product> products = repository.findAll();
        log.info("Fetching list of products");
        return products.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponseDTO getProduct(Long id) {
        Product product = repository.findById(id).orElseThrow(()-> {
           log.info("Product with id: {} not found",id);
           return new RuntimeException("Product with "+ id +" is not exist!!");
        });
        return mapper.toResponse(product);
    }

    @Override
    @Transactional
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO dto) {
      Product product = repository.findById(id).orElseThrow(()-> new RuntimeException("Product does not exist"));
      product.setActive(dto.getActive());
      product.setStockQuantity(dto.getStockQuantity());
      product.setDescription(dto.getDescription());
      product.setPrice(dto.getPrice());
      product.setName(dto.getName());
      product.setSku(dto.getSku());

       log.info("Product with sku:{} is updated with id:{}",product.getSku(), product.getProductId());
       return mapper.toResponse(product);
    }

    @Override
    public ProductResponseDTO addProduct(ProductRequestDTO dto) {
        repository.findBySku(dto.getSku()).ifPresent(exist ->{
                log.warn("Product with sku:{} already exist",dto.getSku());
        throw new RuntimeException("Product with sku: "+dto.getSku()+" already exists");
        });

        var product = mapper.toEntity(dto);
        var savedProduct = repository.save(product);
        log.info("Product added successfully with id: {}", savedProduct.getProductId());
        return mapper.toResponse(savedProduct);
    }

    @Override
    public boolean deleteProduct(Long id) {
       Product product = repository.findById(id).orElseThrow(()->{
           log.warn("Tried to delete non-existent product with id: {}", id);
           throw new RuntimeException("Product with id: "+ id +" does not exist");
       });

       repository.delete(product);
       log.info("Product with id:{} deleted !",product.getProductId());
       return true;
    }
}
