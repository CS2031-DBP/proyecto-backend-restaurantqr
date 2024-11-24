package com.example.proydbp.product.infrastructure;

import com.example.proydbp.client.domain.Rango;
import com.example.proydbp.product.domain.Category;
import com.example.proydbp.product.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByCategory(Category category, Pageable pageable);  // Cambiado a Page<Product>

    Page<Product> findByRango(Rango rango, Pageable pageable);  // Cambiado a Page<Product>

    Page<Product> findByIsAvailable(boolean isAvailable, Pageable pageable);  // También ajustado para paginación
}