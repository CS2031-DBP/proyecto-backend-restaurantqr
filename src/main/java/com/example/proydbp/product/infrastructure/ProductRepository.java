package com.example.proydbp.product.infrastructure;

import com.example.proydbp.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
