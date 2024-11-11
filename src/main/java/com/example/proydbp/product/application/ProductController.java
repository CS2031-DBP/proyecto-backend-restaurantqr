package com.example.proydbp.product.application;

import com.example.proydbp.product.domain.ProductService;
import com.example.proydbp.product.dto.ProductRequestDto;
import com.example.proydbp.product.dto.ProductResponseDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    final private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long id) {
        ProductResponseDto productResponseDto = productService.findProductById(id);
        return ResponseEntity.ok(productResponseDto);
    }

    @GetMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        List<ProductResponseDto> productResponseDtos = productService.findAllProducts();
        return ResponseEntity.ok(productResponseDtos);
    }

    @PostMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ProductResponseDto> createProduct(@Valid @RequestBody ProductRequestDto dto) {
        ProductResponseDto createdProduct = productService.createProduct(dto);
        return ResponseEntity.status(201).body(createdProduct);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequestDto dto) {
        ProductResponseDto updatedProduct = productService.updateProduct(id, dto);
        return ResponseEntity.ok(updatedProduct);
    }

    @GetMapping("/category/{category}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<ProductResponseDto>> getProductByCategory(@PathVariable String category) {
        List<ProductResponseDto> productResponseDto = productService.findByCategory(category);
        return ResponseEntity.ok(productResponseDto);
    }

    @PatchMapping("/changeAvailability/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ProductResponseDto> changeProductAvailability(@PathVariable Long id) {
        ProductResponseDto updatedProduct = productService.changeAvailability(id);
        return ResponseEntity.ok(updatedProduct);
    }


    @GetMapping("/available")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<ProductResponseDto>> getAvailableProducts() {
        List<ProductResponseDto> availableProducts = productService.findAvailableProducts();
        return ResponseEntity.ok(availableProducts);
    }
}
