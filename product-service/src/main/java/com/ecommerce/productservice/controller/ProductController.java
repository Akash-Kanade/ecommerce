package com.ecommerce.productservice.controller;

import com.ecommerce.productservice.dto.ProductRequestDTO;
import com.ecommerce.productservice.dto.ProductResponseDTO;
import com.ecommerce.productservice.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("product")
public class ProductController {
    private final ProductService productService;
    private final String DELETE_MSG = "Product deleted successfully.";

    ProductController(ProductService productService)
    {
        this.productService = productService;
    }

    @GetMapping("/getall")
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts()
    {
        var response = productService.getAllProducts();
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }


    @GetMapping("/search")
    public ResponseEntity<List<ProductResponseDTO>> findByProductCategory(@RequestParam String name)
    {
        log.info("Request Parameter: {}",name);
        var response = productService.searchProduct(name);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ProductResponseDTO> getProduct(@PathVariable long id){
       var response =  productService.getProduct(id);
       return ResponseEntity.status(HttpStatus.OK)
               .body(response);
    }

    @PostMapping("/add")
    public ResponseEntity<ProductResponseDTO> addProduct(@RequestBody ProductRequestDTO dto)
    {
       var response =  productService.addProduct(dto);
       return ResponseEntity.status(HttpStatus.CREATED)
               .body(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable Long id, @RequestBody ProductRequestDTO dto)
    {
        var response = productService.updateProduct(id, dto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id)
    {
        productService.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(DELETE_MSG);
    }
}
