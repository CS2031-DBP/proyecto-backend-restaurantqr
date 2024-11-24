package com.example.proydbp.product.application;

import com.example.proydbp.client.domain.Rango;
import com.example.proydbp.product.domain.Category;
import com.example.proydbp.product.domain.ProductService;
import com.example.proydbp.product.dto.ProductRequestDto;
import com.example.proydbp.product.dto.ProductResponseDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
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
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findProductById(id));
    }



    @PostMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ProductResponseDto> createProduct(@Valid @RequestBody ProductRequestDto dto) {
        return ResponseEntity.status(201).body(productService.createProduct(dto));
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
        return ResponseEntity.ok(productService.updateProduct(id, dto));
    }


    @PatchMapping("/changeAvailability/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CHEF')")
    public ResponseEntity<ProductResponseDto> changeProductAvailability(@PathVariable Long id) {
        ProductResponseDto updatedProduct = productService.changeAvailability(id);
        return ResponseEntity.ok(updatedProduct);
    }

    @GetMapping("/categories")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<List<String>> getProductCategories() {
        List<String> categories = Arrays.stream(Category.values())
                .map(Enum::name)
                .toList();
        return ResponseEntity.ok(categories);
    }


    @GetMapping("/available")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<Page<ProductResponseDto>> getAvailableProducts(@RequestParam int page, @RequestParam int size) {
        Page<ProductResponseDto> availableProducts = productService.findAvailableProducts(page, size);
        return ResponseEntity.ok(availableProducts);
    }

    @GetMapping()
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<Page<ProductResponseDto>> getAllProducts(@RequestParam int page, @RequestParam int size) {
        Page<ProductResponseDto> allProducts = productService.findAllProducts(page, size);
        return ResponseEntity.ok(allProducts);
    }

    @GetMapping("/category/{category}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<Page<ProductResponseDto>> getProductByCategory(@PathVariable Category category, @RequestParam int page, @RequestParam int size) {
        Page<ProductResponseDto> productResponseDto = productService.findByCategory(category, page, size);
        return ResponseEntity.ok(productResponseDto);
    }

    @GetMapping("/rango/{rango}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<Page<ProductResponseDto>> getProductsByClientRango(@PathVariable Rango rango, @RequestParam int page, @RequestParam int size) {
        Page<ProductResponseDto> productResponseDto = productService.findProductByClientRango(rango, page, size);
        return ResponseEntity.ok(productResponseDto);
    }


}
