package com.example.proydbp.product.domain;

import com.example.proydbp.exception.ResourceNotFoundException;
import com.example.proydbp.pedido_local.domain.StatusPedidoLocal;
import com.example.proydbp.product.dto.PatchProductDto;
import com.example.proydbp.product.dto.ProductRequestDto;
import com.example.proydbp.product.dto.ProductResponseDto;
import com.example.proydbp.product.infrastructure.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
        // Mapeo usando ModelMapper
        Product product = modelMapper.map(dto, Product.class);
        Product savedProduct = productRepository.save(product);

        // Mapea el producto guardado a ProductResponseDto y retorna
        return modelMapper.map(savedProduct, ProductResponseDto.class);
    }

    public ProductResponseDto findProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
        return modelMapper.map(product, ProductResponseDto.class);
    }

    public List<ProductResponseDto> findAllProducts() {
        return productRepository.findAll().stream()
                .map(product -> modelMapper.map(product, ProductResponseDto.class))
                .collect(Collectors.toList());
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id " + id);
        }
        productRepository.deleteById(id);
    }

    public ProductResponseDto updateProduct(Long id, PatchProductDto dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));

        // Actualizar atributos solo si no son nulos
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

        Product updatedProduct = productRepository.save(product);
        return modelMapper.map(updatedProduct, ProductResponseDto.class);
    }

    public List<ProductResponseDto> findByCategory(String category) {
        List<Product> products = productRepository.findByCategory(Category.valueOf(category));
        return products.stream()
                .map(product -> modelMapper.map(product, ProductResponseDto.class))
                .collect(Collectors.toList());
    }

    public ProductResponseDto changeAvailability(Long id, PatchProductDto dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        // Asumiendo que en PatchProductDto tienes un campo `isAvailable`
        if (dto.getIsAvailable() != null) {
            product.setIsAvailable(dto.getIsAvailable());
        }

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
