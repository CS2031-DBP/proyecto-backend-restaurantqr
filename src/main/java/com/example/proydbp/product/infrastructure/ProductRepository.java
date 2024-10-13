package com.example.proydbp.product.infrastructure;

import com.example.proydbp.product.domain.Category;
import com.example.proydbp.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategory(Category category);

    List<Product> findByIsAvailable(boolean isAvailable);
}
