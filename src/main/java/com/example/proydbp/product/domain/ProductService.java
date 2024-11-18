package com.example.proydbp.product.domain;

import com.example.proydbp.exception.ResourceNotFoundException;
import com.example.proydbp.product.dto.ProductRequestDto;
import com.example.proydbp.product.dto.ProductResponseDto;
import com.example.proydbp.product.infrastructure.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    final private ProductRepository productRepository;
    final private ModelMapper modelMapper;

    @Autowired
    public ProductService(ProductRepository productRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    public ProductResponseDto createProduct(ProductRequestDto dto) {
        Product product = new Product();
        product.setNombre(dto.getNombre());
        product.setDescripcion(dto.getDescripcion());
        product.setPrice(dto.getPrice());
        product.setCategory(dto.getCategory());
        product.setIsAvailable(dto.getIsAvailable());

        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct, ProductResponseDto.class);
    }

    public ProductResponseDto findProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto con id " + id +  " no encontrado"));
        return modelMapper.map(product, ProductResponseDto.class);
    }

    public List<ProductResponseDto> findAllProducts() {
        return productRepository.findAll().stream()
                .map(product -> modelMapper.map(product, ProductResponseDto.class))
                .collect(Collectors.toList());
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Producto con id " + id +  " no encontrado");
        }
        productRepository.deleteById(id);
    }

    public ProductResponseDto updateProduct(Long id, ProductRequestDto dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto con id " + id +  " no encontrado"));

        if (dto.getNombre() != null) {
            product.setNombre(dto.getNombre());
        }
        if (dto.getDescripcion() != null) {
            product.setDescripcion(dto.getDescripcion());
        }
        if (dto.getCategory() != null) {
            product.setCategory(Category.valueOf(String.valueOf(dto.getCategory())));
        }
        if (dto.getIsAvailable() != null) {
            product.setIsAvailable(dto.getIsAvailable());
        }
        if (dto.getPrice() != null) {
            product.setPrice(dto.getPrice());
        }

        Product updatedProduct = productRepository.save(product);
        return modelMapper.map(updatedProduct, ProductResponseDto.class);
    }

    public List<ProductResponseDto> findByCategory(String category) {
        List<Product> products = productRepository.findByCategory(Category.valueOf(category));
        return products.stream()
                .map(product -> modelMapper.map(product, ProductResponseDto.class))
                .collect(Collectors.toList());
    }

    public ProductResponseDto changeAvailability(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto con id " + id +  " no encontrado"));

        product.setIsAvailable(!product.getIsAvailable());

        Product updatedProduct = productRepository.save(product);
        return modelMapper.map(updatedProduct, ProductResponseDto.class);
    }

    public List<ProductResponseDto> findAvailableProducts() {
        List<Product> availableProducts = productRepository.findByIsAvailable(true);
        return availableProducts.stream()
                .map(product -> modelMapper.map(product, ProductResponseDto.class))
                .collect(Collectors.toList());
    }
}
